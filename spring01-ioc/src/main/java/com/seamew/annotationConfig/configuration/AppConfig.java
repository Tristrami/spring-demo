package com.seamew.annotationConfig.configuration;

import com.seamew.annotationConfig.component.Screwdriver;
import com.seamew.annotationConfig.component.Spanner;
import com.seamew.annotationConfig.component.Toolbox;
import com.seamew.annotationConfig.entity.A;
import com.seamew.annotationConfig.entity.B;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

/* @Component, @Repository, @Service, @Controller, @Configuration, @Bean
 * 这几个注解都可以用来向 spring 容器中注入 bean, 用法和区别如下:
 * 1. @Component @Repository @Service @Controller @Configuration := <bean id="" class="">,
 *    它们属于类级注解, 用来声明一个 beanDefinition, spring 会把它有 @Component 注解的类视作一个组件,
 *    实际上就是一个 bean.
 *
 * 2. @Bean := <bean id="" factory-bean="" factory-method=""> @Bean 只能放在方法上, 表示这个方法
 *    是创建 bean 的一个工厂方法. @Bean 通常配合着 @Configuration 使用, 可以用来把其它 jar 包中的类注入到
 *    spring 容器中. @Bean 中还可以使用 init-method 和 destroy-method 属性来指定对应的生命周期回调函数
 *
 * 3. @Configuration: 指定一个对象是 beanDefinition 的源, 相当于一个 applicationContext.xml 配置文件.
 *
 * 🌟 以上的注解都有 @Component 元注解, @Component 中可以指定 value 属性作为 bean 的 id
 *
 * 🌟 @Repository, @Service, @Controller 本质上都是 @Component, 但语意上不同, 后续 spring
 *    也会对这三个注解提供不同的支持
 *
 * 🌟 这些注解和 @Autowired @Resource 的区别:
 *    (1) 这几个注解可以和 @Autowired @Resource 配合使用为组件注入依赖, 但使用这几个注解时默认会自动装配
 *    (2) 这几个注解可以理解为我们告诉 spring 哪些 bean 要放入 spring 容器中, 以及每个 bean 的角色是什么,
 *        需要哪些依赖, 依赖怎么注入. 解决的是哪些 bean 要放入 spring 容器以及 bean 如何创建的问题, 相当于
 *        一个 beanDefinition, 即 <bean/>
 *    (3) @Autowired @Resource 可以理解为容器在创建一个对象时, 我们告诉 spring 如何在容器中寻找合适的
 *        bean, 通过哪种方式往对象中注入依赖. 解决的是怎么注入依赖, 注入什么依赖的问题
 *
 * 🌟 注解配置方式和 xml 配置方式可以相互结合, 我们可以在 xml 文件中申明 @Configuration 类的 beanDefinition,
 *    这是以 xml 为中心的配置方式, 也可以在 @Configuration 类中使用 @ImportResource 注解引入 xml 中的
 *    beanDefinition, 这是以注解为中心的配置方式. spring 的注解并不是为了完全取代 xml 配置文件, 在某些场景下,
 *    比如配置文件中的内容可能经常会变化时, 使用 xml 配置通常更加方便, 因为如果使用注解配置, 配置更改后需要重新编译,
 *    而 xml 不需要. 比较典型的场景就是数据源的相关配置. 以 DataSourceConfig 作为例子
 *
 *    🔎 在 xml 中引入 AppConfig
 *      (1) 在 xml 中引入 @Configuration 类的 beanDefinition: <bean class="xxx.xxx.AppConfig"/>
 *      (2) 在 xml 中引入 .properties 文件: <context:property-placeholder location="classpath:/..."/>
 *          这个标签告诉 spring 要用哪个 properties 文件中的属性填充 xml 文件中的 ${...} 占位符.
 *          引入 .properties 文件后可以使用表达式 ${jdbc.username} 的形式引用 .properties 文件中的变量.
 *      (3) 使用 xml 时不要忘记加 <context:component-scan base-package=""/> 开启自动扫包
 *
 *    🔎 在 AppConfig 中引入 xml
 *      (1) 在 @Configuration 类中引入 xml 中的 beanDefinition: @ImportResource(classpath:/...)
 *      (2) 可以在引入的 xml 文件中使用 <context:property-placeholder/> 标签, 那么可以在 AppConfig
 *          类中的 spring 注解中使用 ${...} 占位符
 *
 * 🌟 lite 模式和 full 模式: 声明在 @Configuration 类中的 @Bean 方法, 会被 spring 使用 full 模式处理,
 *    而声明在其他地方, 即没有 @Configuration, 有 @Component | @Import | @ImportResource | @ComponentScan
 *    注解的类, 或者 @Configuration(proxyBeanMethods = false) 的类, 以及 POJO 中的 @bean 方法, 会被使用 lite
 *    模式处理.
 *    (1) full 模式: spring 在 full 模式下使用 @Bean 方法创建的对象时, 会用 cglib 为 @Bean 方法所在的类生成
 *        动态代理, 这个时候这个放在 spring 容器中的是这个类的代理对象而不是这个类本身. 如果配置类中的一个 @Bean
 *        方法 a 尝试调用另一个 @Bean 方法 b 来获得一个 b 创建的对象, 即 a 依赖 b 时, 那么这个调用会被代理拦截,
 *        被重定向到 spring 容器的生命周期管理中, spring 会在容器中寻找合适的依赖并注入, 而不让我们自己声明依赖
 *
 *    (2) lite 模式: 与 full 模式不同, spring 在 lite 模式下使用 @Bean 方法创建的对象时, 不会为 @Bean 方法
 *        所在类生成代理对象, 这个时候这个放在 spring 容器中的就是这个类本身. lite 模式下, 如果配置类中的一个 @Bean
 *        方法在创建对象尝试调用另一个 @Bean 方法创建对象来为自己注入依赖, 即显式的申明了依赖, 这个调用并不会被拦截,
 *        那么这个时候使用的依赖是我们自己创建的, 而不是 spring 容器中的, 所以 lite 模式下不可以显式的声明依赖
 *
 *    🔎 两种模式的特点及优缺点:
 *       lite:
 *       特点: 放入容器的是配置类本身
 *       优点: 由于不生成代理对象, 性能更好, @Bean 方法可以被 private | final 修饰
 *       缺点: 不能声明依赖, 即不能在 @Bean 方法中调用另外的 @Bean 方法 (把依赖的 bean 放入 @Bean 方法形参即可)
 *
 *       full:
 *       特点: 放入容器的是配置类的代理对象
 *       优点: 1.在 @Bean 方法中调用另外的 @Bean 方法来声明依赖时调用会被拦截, spring 会把这个调用重定向为去容器
 *              中寻找依赖并注入
 *            2. 由于拦截了 @Bean 方法之间的相互调用, spring 可以保证依赖都来自于容器, 可以帮助我们规避一些错误
 *
 *       缺点: 1. 由于使用了动态代理, 会产生一些性能上的开销
 *            2. 由于使用了动态代理, @Bean 方法不可以被 private | final 修饰
 *
 * 🌟 @ComponentScan 中 @Filter 属性的用法:
 *    为 @Component 注解添加 includeFilters = @Filter(...) 和 excludeFilters = @Filter(...)
 *    属性可以自定义扫包时要包含或排除哪些类, @Filter 的 type 属性可以指定筛选的方式, type 可以是:
 *
 *    1. annotation: 通过类上的注解进行筛选, 默认为这种方式.
 *       eg. @Filter({Repository.class, Controller.class})
 *
 *    2. assignable: 筛选实现了相应接口或继承自相应超类的类.
 *       eg. @Filter(type = "FilterType.ASSIGNABLE", classes = {Super.class, SomeInterface.class})
 *
 *    3. regex: 通过正则表达式来匹配要被筛选的类的全限定名称.
 *       eg. @Filter(type = "FilterType.REGEX", pattern = "com\.example\.exclude.*")
 *
 *    4. custom: 使用实现了 org.springframework.core.type.TypeFilter 接口的类进行筛选
 *       eg. @Filter(type = "FilterType.CUSTOM", classes = MyFilter.class)
 *
 *    5. aspectj: 通过 aspectj 表达式进行筛选
 *
 * 其它重要的注解:
 * 1. @ComponentScan: 用于扫包, spring 会包包中所有带有上面注解的目标注册到容器中
 * 2. @Value: 用于自动注入时给原语类型的属性赋值
 * 3. @Scope: 用来指定 bean 的作用范围
 * 4. @Lazy: 可以配置 bean 的懒加载
 * 5. @Import: 可以在一个 @Configuration 类中引入其它 @Configuration 类中的 beanDefinition
 * 6. @ImportResource: 可以在一个 @Configuration 类中引入 xml 配置文件中的 beanDefinition
 * 7. @Description: 可以为 bean 提供一段描述信息,  当bean被公开 (JMX) 用于监视目的时, 可能特别有用 */

@Configuration
@Import(DataSourceConfig.class)
@ComponentScan
(
    basePackages = "com.seamew.annotationConfig",
    excludeFilters = @ComponentScan.Filter
    (
        type = FilterType.REGEX,
        pattern = "com\\.seamew\\.annotationConfig\\.exclude.*"
    )
)
public class AppConfig
{
    @Bean
    public A createA()
    {
        // 这里是 full 模式, createB() 方法会被 spring 拦截, spring 会去容器中寻找 B 对象注入给 A
        return new A(createB());
    }

    @Bean(initMethod = "init")
    public B createB()
    {
        return new B();
    }

    @Bean(name = "toolbox-full-tools", initMethod = "init", destroyMethod = "destroy")
    public Toolbox createToolBox(@Value("toolbox") String name, Screwdriver screwdriver, Spanner spanner)
    {
        return new Toolbox(name, screwdriver, spanner);
    }

    @Bean(name = "toolbox-screwdriver-only", initMethod = "init", destroyMethod = "destroy")
    public Toolbox createToolBox(@Value("toolbox") String name, Screwdriver screwdriver)
    {
        return new Toolbox(name, screwdriver);
    }
}
