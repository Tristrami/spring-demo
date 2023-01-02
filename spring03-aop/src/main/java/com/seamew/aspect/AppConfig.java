package com.seamew.aspect;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

/**
 * 切面 (Aspect): 使用在类上加上注解 @Aspect 的方式来声明一个切面, 还需要把切面类注册到容器中
 *
 * 切入点 (Pointcut): 切入点更像是对连接点 (join point) 的说明或者声明, 它通过切点表达来定义了一个
 * 匹配连接点的规则, 这样 spring 就可以知道哪些连接点是我们感兴趣的, 就可以只在通过规则匹配到的连接点
 * 上编织我们声明的 advice. 使用在方法上加上注解 @Pointcut 的方式来声明一个切入点, 在其他地方可以直
 * 接通过方法名来引用这个 pointcut. 声明一个切入点分为两个部分, 第一是方法的签名, 第二是 @Pointcut
 * 中的切点表达式. 方法签名指的是, 加上了 @Pointcut 的无返回值无参的空方法, 这些方法会被视作 pointcut
 * 声明, 就像声明一个变量一样需要给变量起个名字, 我们在这里用方法的签名作为 pointcut 的名字, 在其它地方
 * 就可以像通过变量名引用一个变量那样, 通过方法名去引用一个 pointcut. 除了方法签名之外, 切入点的声明还包
 * 括写在切入点注解 @Pointcut 中的切点表达式. 切点表达式用来用来匹配连接点 (join point), 也就是说,
 * spring需要通过切点表达式来匹配到对应的连接点 (方法), 然后把我们声明的 advice 编织到 (weave) 对应的
 * 连接点上, 来达到我们的目的
 *
 * 连接点 (Join point): 连接点指的是程序运行中的某些特殊节点，例如方法的执行，字段的访问等等都可以称为
 * 连接点. 而 spring aop 中的连接点就是指某个方法的执行, 而方法是定义在类中的, 我们匹配某个连接点, 即
 * 匹配某个方法时, 无非是从三个角度考虑:
 *
 *  (1) 从方法本身考虑: 方法本身具有的属性有方法签名, 修饰符, 抛出的异常等等, 我们可以通过指定方法的
 *      这两个属性来匹配到具体的方法
 *  (2) 从方法所在的类考虑: 匹配方法时可以从类的角度去考虑, 我们可以通过限定类的范围来筛选出我们想要
 *      的方法, 也就是匹配给定类中的所有方法.
 *  (3) 从类所在的包考虑: 方法在类中, 而类在包中, 限定包的范围, 再限定类的范围, 就可以匹配到特定包中
 *      特定类中的所有方法
 *
 * 基于这几个角度, spring 给我们提供了一些匹配连接点 (方法) 的方式
 * 例子详见 https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-pointcuts-examples
 *
 * 1. execution: 语意是 execution of which method, 也就是匹配什么方法, 因为 spring aop 中方法的
 *    执行就是连接点, 所以匹配方法和匹配连接点是等价的, 也就是说这种匹配实际上是通过直接匹配具体的方法来匹
 *    配了连接点. 这是最常用的匹配方法, 包含了包匹配, 类匹配和方法匹配, 用法如下:
 *    execution(modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern) throws-pattern?)
 *    eg. execution("public * com.seamew..service..*.*") 表示 com.seamew 目录下所有 service
 *        包及其子包内的所有类的所有 public 方法
 *
 *    🔍 pattern 的写法: 详见 https://www.eclipse.org/aspectj/doc/released/progguide/semantics-pointcuts.html
 *    🌟 ? 表示这个 pattern 是可选的, 没有的话表示任意, 与方法签名有关的三个属性 (方法名, 参数, 返回值)
 *      的 pattern 都是必要的, 其它都是可选的
 *      (1) modifiers-pattern: 可选, 表示方法的修饰符, 例如 public
 *      (2) ret-type-pattern: 必要, 方法的返回类型. 可以用 * 表示任意返回类型
 *      (3) declaring-type-pattern: 可选, 表示方法所在类的全限定名称, 包名之间用 . 分隔, 我们可
 *          以使用占位符 .. 来表示任意目录, 使用 * 来表示任意类, 例如 com.seamew..service.* 表示
 *          com.seamew 目录下所有 service 包中的所有类. 还可以在类名 (如果有) 后面加上 + 来表示本
 *          类及其子类, 例如 com.seamew.service.IUserService+. 表示 service 包下的 IUserService
 *          类及其子类 declaring-type-pattern 一般会和 name-patter 一起用, 表示方法的全限定名称,
 *          如 com.seamew.service.userService(..)
 *      (4) name-pattern: 必要, 表示方法名. 可以用 * 来表示任意方法, 也可以在方法名中间使用 *, 例如
 *          *Service 表示任意以 Service 结尾的方法. name-pattern 一般会和 declaring-type-pattern
 *          一起用, 表示方法的全限定名称, 如 com.seamew.service.userService(..)
 *      (5) param-pattern: 必要, 表示参数列表. 可以用 (..) 来表示任意个数任意类型参数, 也可以使用
 *          (*,String) 来表示两个参数, 且第二个参数为 String 类型的方法
 *      (6) throws-pattern: 表示抛出的异常
 *
 *    🔍 占位符的用法:
 *      (1) * 可以放在任意 pattern 上, 表示匹配所有; 也可以用来替代类名和方法名, 表示任意类或任意方法
 *      (2) .. 可以用在全限定名称的目录名上, 表示任意目录; 也可以用在方法的参数上, 表示任意数量任意类型的参数
 *
 * 2. within: 语意是 join point within ... types, 就是匹配在哪些类中的连接点, 这种匹配方法只匹配在
 *    给定类型范围中的类中的连接点, 也就是说, 只有在给定类中的连接点(方法)才会被匹配到
 *    eg. within(com.seamew.service.*) 表示匹配 service 包下所有类中的所有方法
 *
 * 3. this: 语意是 当前 proxy 对象是某个类的实例, 也就是说只有当当前的代理对象所属类属于给定的类型时,
 *    其中的连接点 (方法) 才能被匹配到.
 *    eg. this(com.seamew.service.UserService), 如果当前代理对象所属的类实现了 IUserService
 *        接口, 那么就去匹配这个类中属于 IUserService 接口的方法
 *
 * 4. target: 语意是 当前 proxy 所代理的目标对象是某个类的实例, 也就是说只有当当前的 Proxy 所代理的目标
 *    对象所属类属于给定的类型 (包括继承和实现), 那么就匹配这个类中属于给定类型中的所有方法. 要特别注意 this
 *    与target 的区别, 在开启了 auto-proxying之后, 放入 spring 容器中的 bean 都不再是 bean 本身, 而
 *    是一个代理对象, 在 spring aop 的切点表达式中, this 特指代理对象, 而 target 特指被代理的目标对象
 *    eg. target(com.seamew.service.IUserService), 如果当前被代理对象所属的类实现了 IUserService
 *        接口, 那么就去匹配这个类中属于 IUserService 接口的方法
 *
 * 🌟 使用 this 和 target 时要注意, 匹配类中的方法时并不一定会匹配到所有的方法, 例如表达式
 *    target(com.seamew.Interface1), 有一个类 Target 实现了 Interface1 和 Interface2,
 *    这时只会匹配 Target 中属于 Interface1 接口的方法, 而不会匹配属于 Interface2 中的方法
 *
 * 5. args: 使用参数列表来匹配方法, 语意是匹配参数类型是给定类型实例的方法.
 *    eg. args(java.io.Serializable, ..) 表示匹配在运行时 (runtime), 第一个参数为 Serializable 接
 *    口的实例的方法
 *
 *    还有另一种用法:
 *    @Pointcut("args(argName,..)")
 *    public void somePointcut(ArgType arg) {}
 *    这个匹配语意相当于 args(com.abc.ArgType), 可以匹配到参数中有 ArgType 类型参数的方法, 使用这种写法
 *    时, 可以让这个 pointcut 成为一个参数的提供者, 其它 advice 方法如果使用这个切点, 就可以把它提供的参数
 *    注入到 advice 方法对应的参数中, 这样可以实现在 advice 方法中使用连接点方法中的参数
 *
 *    eg. @Pointcut("args(argName,..)")
 *        public void somePointcut(ArgType arg) {}
 *
 *        @Before(value = "somePointcut", argNames = "argName")
 *        public void someAdvice(ArgType arg) {...}
 *
 *        在这个例子中, spring 会将连接点方法相应的参数注入给 someAdvice() 方法中的 arg 参数, 这样我们就
 *        可以在 someAdvice() 方法中使用 arg 参数了
 *
 * 6. @target: 语意是, 被代理的对象所属的类上有给定的注解类型的注解. 也就是通过类上的注解类型来匹配类,
 *    并且匹配类中的所有方法
 *    eg. @target(org.springframework.transaction.annotation.Transactional), 如果当前被代理对象
 *        所属的类上面有 @Transactional 注解, 那么就匹配其中的所有方法
 *
 * 7. @within: 语意是 join point within ... types, 但与 within 不同, @within 通过类上的注解的
 *    类型进行匹配, 如过当前类上的注解类型属于给定的注解类型, 那么就匹配这个类中的所有方法
 *    eg. @within(org.springframework.transaction.annotation.Transactional), 如果当前类上面
 *        有 @Transactional 注解, 那么就匹配其中的所有方法
 *
 *  🌟 @target 和 @within 在匹配时效果是相同的
 *
 * 8. @annotation: 语意是, 方法上有给定注解类型的注解, 也就是按照方法上的注解类型来匹配
 *    eg. @annotation(org.springframework.transaction.annotation.Transactional), 匹配所有
 *        拥有 @Transactional 注解的方法
 *
 * 9. @args: 语意是, 方法的参数上有给定注解类型的注解, 也就是按参数上的注解类型来匹配方法
 *    eg. @args(java.io.Serializable, ..), 如果当前的方法的第一个参数上有 @Serializable 注解,
 *        那么这个方法就会被匹配到
 *
 * 10. bean(beanName): 通过 bean 的名字匹配 spring 容器中对应 bean 的所有方法
 *     eg. bean(userService), 匹配 spring 容器中名字为 userService 的 bean 中的所有方法
 *
 * 11. bean(namePattern): 使用 namePattern 指定 bean 的名字来匹配 spring 容器中对应 bean 的
 *     所有方法
 *     eg. bean(*Service), 匹配 spring 容器中名字以 Service 结尾的 bean 中的所有方法
 *
 * 切点表达式的运算: 我们声明的切点表达式可以通过逻辑运算符号进行逻辑运算 (! || &&)
 *
 * 总结:
 * 1. execution 是最常用最精确的匹配, 可以满足通过方法修饰符, 包名, 类名, 方法签名来进行灵活的匹配
 * 2. within, this, target, 都是通过类来匹配方法, within 匹配满足条件的任何类中的所有方法, this 匹配
 *    满足条件的代理对象中的所有方法, target 匹配满足条件的被代理对象中的所有方法
 * 3. @target, @within, @annotation, @args 都是通过注解的类型来匹配方法, @target 和 @within 以类
 *    上的注解类型作为条件来匹配拥有同类型注解的类中的所有方法, @annotation 以方法上的注解作为条件来匹配拥
 *    有同类型注解的方法, @args 以方法参数的注解类型作为条件来匹配参数上有同类型注解的方法. 这几个以 @ 开
 *    头的匹配模式, 依次匹配 类 -> 方法 -> 方法参数 上的注解
 *
 * spring 将这些 designator 分成了三类, kinded, scoping, contextual
 * 1. kinded: kinded designator, 如 execution, 匹配同一种类的方法. 同一种类指的是, 这些方法有一些共同
 *    点, 比如它们都是业务类的方法, 或者都是数据库访问的方法
 * 2. scoping: scoping designator, 如 within, 在特定范围内匹配一组方法. 这里的特定范围内也就是特定作用
 *    域内, 换句话说, 这种匹配方法划定一个作用域范围 (其实就是指定一些类), 只匹配作用范围中的方法 (也就是只匹
 *    配给定类中的方法), 它们不一定有共同特征, 但是都在共同的作用域中, 所以在结构上我们可以把这些方法看作一组
 *    方法
 * 3. contextual: contextual designator, 如 this, target, @annotation, 根据给定的上下文信息来匹配
 *    到相应的方法. 这里的上下文指的是对象上或方法上的一些元数据信息, 我们提供一份元数据信息作为上下文, spring
 *    就为我们去匹配满足上下文描述的方法. 例如, this(com.seamew.A) 指定当前代理对象需要是 A 类的实例, 条件
 *    com.seamew.A 和按照代理对象去筛选的这个方式, 就是我们提供的上下文信息.
 * 🌟 spring 建议我们至少要包含 kinded 和 scoping 这两种 designator 来保证匹配的效率, 并且, scoping
 *    类型的 designator 匹配效率最高, 我们可以把一些非常常用或者基本的 pointcut 使用 within 声明, 例如
 *    service 目录中的所有方法, dao 目录中的所有方法, 这样我们以后使用其它 designator 来匹配这些方法时,
 *    spring 无需再进行额外处理, 可以提高匹配效率
 *    例子见 https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-common-pointcuts
 *
 * 🌟 一定要注意 spring AOP 是基于动态代理的, 通过把目标对象包装在代理中来实现对方法的增强, 所以只有调用代理
 *   对象的方法时 advice 才会生效, 如果目标对象的方法使用了 this.someMethod() 这样的本地调用, advice 不
 *   会生效, 因为这样就不是调用代理对象的方法了. 而 AspectJ 是使用编织器通过修改字节码的方式来对方法进行相应
 *   的增强, 没有代理对象, 所以使用 this 进行本地方法调用 advice 也会生效 */

@EnableAspectJAutoProxy
@ComponentScan("com.seamew")
@Configuration
public class AppConfig
{

}
