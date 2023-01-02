import com.seamew.spel.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class TestSpEL
{
    @Test
    public void testHelloWorld()
    {
        /* SpEL 解析表达式时会先对表达式进行词法分析, 语法分析, 将语法树和分词流封装到
         * Expression 对象中, 然后 spring 会在 EvaluationContext 中调用抽象语法树
         * 进行解析求值, 类型转换, 最终得到结果. EvaluationContext 中包含了自定义变量,
         * 自定义函数, 根对象, 类型转换器等等
         *
         * SpEL 相关配置:
         * 1. SpelParserConfiguration: 配置自动初始化 null 引用和自动数组, 集合扩容,
         *    在 SpelExpressionParser 的构造函数中使用
         * 2. EvaluationContext: 配置自定义变量, 函数, 对象属性访问权限 (通过使用
         *    SimpleEvaluationContext 的 Builder 来构造拥有只读或可读可写权限的 context),
         *    在 ExpressionParser 的 getValue() 和 setValue() 方法中使用
         * 3. ParserContext: 配置 SpEL 表达式的前缀和后缀, 在 ExpressionParser 的
         *    parseExpression() 方法中使用
         * 🌟 context 用于给方法执行提供上下文, configuration 用于配置构建要构建的对象 */

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'hello world'");
        String value = (String) exp.getValue();
        log.debug("The string is [{}]", value);
    }

    @Test
    public void testConcat()
    {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp =  parser.parseExpression("'hello '.concat('world')");
        String value = (String) exp.getValue();
        log.debug("The string is [{}]", value);
    }

    @Test
    public void testLength()
    {
        String s = "hello world";
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'" + s + "'.length()");
        Integer len = (Integer) exp.getValue();
        log.debug("The length of string [{}] is [{}]", s, len);
    }

    @Test
    public void testBytes()
    {
        String s = "hello world";
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'" + s + "'.bytes");
        byte[] bytes = (byte[]) exp.getValue();
        log.debug("The byte array of string [{}] is [{}]", s, bytes);
    }

    @Test
    public void testNestedProperty()
    {
        String s = "'hello world'.bytes.length";
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(s);
        Integer len = (Integer) exp.getValue();
        log.debug("The value of nested property [{}] is [{}]", s, len);
    }

    @Test
    public void testCallConstructor()
    {
        String s = "new String('hello world')";
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(s);
        String value = exp.getValue(String.class);
        log.debug("The result of calling [{}] is [{}]", s, value);
    }

    @Test
    public void testRootObject()
    {
        // SpEL 可以针对特定的对象, 解析表达式, 这个对象称为根对象
        // 例如下面的例子用表达式 "username" 检索 user 对象中的 username 属性
        // 同理, 我们可以用表达式 "getUsername()" 检索 user 对象中的 getUsername() 方法
        // SpEL 还支持通过表达式获得某个对象引用, 然后使用 exp.setValue() 赋值
        User user = new User("lulu", "123");
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("username");
        String s = exp.getValue(user, String.class);
        log.debug("The username is [{}]", s);
    }

    @Test
    public void testEvaluationContextForReadOnlyDataBinding()
    {
        /* EvaluationContext 用于执行表达式的时候解析其中的属性, 方法, 字段以及
         * 执行类型转换. spring 给我们提供了两个实现:
         * 1. SimpleEvaluationContext: 只提供了对 SpEL 部分的语法, 特性和配置选项的支持
         * 2. StandardEvaluationContext: 提供了对 SpEL 全部语法, 特性, 配置选项的支持,
         *    SpEL 默认使用 StandardEvaluationContext
         * 我们可以通过 SimpleEvaluationContext 中的 Builder 构建我们需要的 context,
         * 例如, 我们可以调用 SimpleEvaluationContext.forReadOnlyDataBinding().build()
         * 来构建一个对对象属性只可读的 EvaluationContext, 之后用这个 context 为 Expression
         * 的一些方法提供上下文配置, 例如 setValue(), getValue() 可以传入 context.
         * 🌟 使用 SimpleEvaluationContext 时不支持在 SpEL 中进行对象方法调用, 不支持使用
         *    T() 来获取 class 对象 */

        Check check = new Check();
        check.setFlag(false);
        String fieldName = "flag";
        // 创建一个对属性只可读的 EvaluationContext
        EvaluationContext ctx = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(fieldName);
        try
        {
            // 尝试修改属性 "flag" 时会抛出异常
            exp.setValue(ctx, check, "true");
        }
        catch (SpelEvaluationException e)
        {
            log.error("Field [{}] is not writable. The access level to field configured in EvaluationContext is [read only]\n", fieldName, e);
        }
    }

    @Test
    public void testEvaluationContextForReadWriteDataBinding()
    {
        Check check = new Check();
        check.setFlag(false);
        // 创建一个对属性可读可写的 EvaluationContext
        EvaluationContext ctx = SimpleEvaluationContext.forReadWriteDataBinding().build();
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("flag");
        // 尝试修改属性 "flag"
        exp.setValue(ctx, check, "true");
        // 属性 "flag" 修改成功
        log.debug("The flag is [{}]", check.getFlag());
    }

    @Test
    public void testTypeConversionByConversionService()
    {
        // EvaluationContext 会使用 ConversionService 对我们传递过去的 value 进行类型转换
        Check check = new Check();
        check.setFlag(false);
        EvaluationContext ctx = SimpleEvaluationContext.forReadWriteDataBinding().build();
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("flag");
        // 用 ConversionService 将字符串 "true" 转化为布尔类型 true
        exp.setValue(ctx, check, "true");
        log.debug("The flag is [{}]", check.getFlag());
    }

    @Test
    public void testSpelParserConfiguration()
    {
        /* 我们可以使用 SpelParserConfiguration 来对 SpelExpressionParser 进行配置,
         * 主要可以配置两个功能:
         * 1. 自动初始化 null 引用: 当执行表达式遇到 null 引用时, spring 会调用这个类默认的
         *    构造器创建一个对象
         * 2. 集合, 数组自动扩容: 当对集合类对象或数组索引越界时 spring 会为我们自动扩容 */

        BuckList buckList = new BuckList();

        // 两个参数分别表示:
        // - 自动初始化 null 引用
        // - 集合, 数组自动扩容
        SpelParserConfiguration config = new SpelParserConfiguration(true, true);
        ExpressionParser parser = new SpelExpressionParser(config);
        Expression exp = parser.parseExpression("list[2]");
        exp.setValue(buckList, "travel");
        log.debug("The buckList is [{}]", buckList.getList());
    }

    @Test
    public void testSpELCompiler()
    {
        /* SpEL 对表达式的解析有解释器 (Interpreter) 和编译器 (Compiler) 模式,
         * 编译器模式下 spring 会把表达式解析为可执行的字节码文件, 相较于解释器模式
         * 性能会有大幅提升. 但在 compiler 模式下, 有以下几种表达式不可编译:
         * 1. 有赋值行为的表达式
         * 2. 需要使用 ConversionService 进行类型转化的表达式
         * 3. 使用自定义 resolver 和 accessor 的表达式
         * 4. 使用了 selection (选择) 和 projection (投影) 的表达式
         *
         * 我们可以通过 SpelParserConfiguration 来配置 SpEL 解析器的模式:
         * 1. OFF: 不启用编译模式
         * 2. MIXED: 在混合模式下, 随着时间的推移, 表达式会从解释模式自动切换到编译模式.
         *    即前面使用解释模式，当调用次数达到某个阈值后，改为使用编译模式
         * 3. IMMEDIATE: 在这个模式下表达式会尽早地被编译. 实际上在第一次使用了解释器之后
         *    才会被编译 */

        User user = new User("lulu", "123");
        SpelParserConfiguration config = new SpelParserConfiguration(
                SpelCompilerMode.IMMEDIATE,
                this.getClass().getClassLoader());
        SpelExpressionParser parser = new SpelExpressionParser(config);
        Expression exp = parser.parseExpression("username");
        String username = exp.getValue(user, String.class);
        log.debug("The username is [{}]", username);
    }

    @Test
    public void testSpELInXmlConfig()
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spel/spel.xml");
        BuckList list = ctx.getBean(BuckList.class);
        log.debug("The buckList is [{}]", list.getList());
    }

    @Test
    public void testSpELInAnnotationConfig()
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        BuckList buckList = ctx.getBean(BuckList.class);
        log.debug("The buckList is [{}]", buckList.getList());
    }

    @Test
    public void testLiteralExpressions()
    {
        // 解析字面量表达式
        ExpressionParser parser = new SpelExpressionParser();
        // 解析字符串字面量
        String helloWorld = parser.parseExpression("'hello world'").getValue(String.class);
        // 解析科学计数法
        Double avogadrosNumber = parser.parseExpression("6.0221415E+23").getValue(Double.class);
        // 进制转换
        Integer maxValue = parser.parseExpression("0x7fffffff").getValue(Integer.class);
        // null 值
        Object nullValue = parser.parseExpression("null").getValue();

        log.debug("The helloWorld is [{}]", helloWorld);
        log.debug("The avogadrosNumber is [{}]", avogadrosNumber);
        log.debug("The maxValue is [{}]", maxValue);
        log.debug("The nullValue is [{}]", nullValue);
    }

    @Test
    public void testPropertyValueAndMethod()
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spel/spel.xml");
        Person person = ctx.getBean(Person.class);

        EvaluationContext evaluationContext = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        ExpressionParser parser = new SpelExpressionParser();

        // 🌟 SpEL 中属性值和方法名的首字母是大小写不敏感的
        // 通过嵌套属性的表达式访问属性值
        String year = parser.parseExpression("birthday.year")
                .getValue(evaluationContext, person, String.class);
        // 嵌套调用方法, 涉及到方法调用时不能使用 SimpleEvaluationContext
        String month = parser.parseExpression("getBirthday().getMonth()")
                .getValue(person, String.class);
        // 通过 [index] 的方式访问数组中的元素
        String hobby = parser.parseExpression("hobbies[0]")
                .getValue(evaluationContext, person, String.class);
        // 通过 [index] 的方式访问 List 中的元素
        String phoneNumber = parser.parseExpression("phoneNumber[0]")
                .getValue(evaluationContext, person, String.class);
        // 通过 [key] 的方式访问 Map 中的元素
        String contact = parser.parseExpression("contacts['ltr']")
                .getValue(evaluationContext, person, String.class);

        log.debug("The year of birthday is [{}]", year);
        log.debug("The month of birthday is [{}]", month);
        log.debug("The hobby is [{}]", hobby);
        log.debug("The phoneNumber is [{}]", phoneNumber);
        log.debug("The contact is [{}]", contact);
    }

    @Test
    public void testInlineListAndMap()
    {
        ExpressionParser parser = new SpelExpressionParser();

        // 在 SpEL 中使用 {...} 可以构建 List, Map, Set 等集合类
        List list = parser.parseExpression("{1,2,3,4}").getValue(List.class);
        Set set = parser.parseExpression("{1,2,3,4}").getValue(Set.class);
        Map map = parser.parseExpression("{name:'lulu',phoneNumber:'18768592825'}").getValue(Map.class);
        log.debug("The list is [{}]", list);
        log.debug("The set is [{}]", set);
        log.debug("The map is [{}]", map);
    }

    @Test
    public void testArrayConstruction()
    {
        ExpressionParser parser = new SpelExpressionParser();
        int[] numbers1 = parser.parseExpression("new int[4]").getValue(int[].class);
        int[] numbers2 = parser.parseExpression("new int[]{1,2,3,4}").getValue(int[].class);
        int[] numbers3 = parser.parseExpression("new int[2][2]").getValue(int[].class);
        // 目前不支持在表达式中使用这种方式初始化多维数组
        // int[] numbers3 = parser.parseExpression("new int[2][2]{{1,2},{3,4}}").getValue(int[].class);

        log.debug("The numbers1 is [{}]", numbers1);
        log.debug("The numbers2 is [{}]", numbers2);
        log.debug("The numbers3 is [{}]", numbers3);
    }

    @Test
    public void testMethodInvocation()
    {
        Check check = new Check();
        ExpressionParser parser = new SpelExpressionParser();
        String string = parser.parseExpression("'happy'.substring(1, 4)").getValue(String.class);
        Boolean flag = parser.parseExpression("getFlag()").getValue(check, Boolean.class);
        log.debug("The string is [{}]", string);
        log.debug("The flag is [{}]", flag);
    }

    @Test
    public void testRelationOperators()
    {
        /* 这些关系符号同样可以使用下面的字母替代, 在配置文件中可能会用到
           lt (<), gt (>), le (<=), ge (>=), eq (==), ne (!=),
           div (/), mod (%), not (!), instanceof, matches
           🌟 SpEL 在比较数字时会直接把基础数字类型装箱, 所以 1 instance of int 的为 false  */

        ExpressionParser parser = new SpelExpressionParser();
        // true
        Boolean equals = parser.parseExpression("1 == 1").getValue(Boolean.class);
        // true
        Boolean lessThan = parser.parseExpression("-2 < 3").getValue(Boolean.class);
        // true
        Boolean greaterThan = parser.parseExpression("'pot' > 'pet'").getValue(Boolean.class);
        // true
        Boolean lessThanAndEquals = parser.parseExpression("1 <= 1").getValue(Boolean.class);
        // 任意 x < null 均为 false
        Boolean lessThanNull = parser.parseExpression("T(Integer).MIN_VALUE < null").getValue(Boolean.class);
        // 任意 x > null 均为 true
        Boolean greaterThanNull = parser.parseExpression("T(Integer).MAX_VALUE > null").getValue(Boolean.class);
        // true
        Boolean instanceOf = parser.parseExpression("'abc' instanceof T(String)").getValue(Boolean.class);
        // true
        Boolean matches = parser.parseExpression("'hello' matches '^[a-z]+$'").getValue(Boolean.class);

        log.debug("The equals is [{}]", equals);
        log.debug("The lessThan is [{}]", lessThan);
        log.debug("The greaterThan is [{}]", greaterThan);
        log.debug("The lessThanAndEquals is [{}]", lessThanAndEquals);
        log.debug("The lessThanNull is [{}]", lessThanNull);
        log.debug("The greaterThanNull is [{}]", greaterThanNull);
        log.debug("The instanceOf is [{}]", instanceOf);
        log.debug("The matches is [{}]", matches);
    }

    @Test
    public void testLogicOperators()
    {
        /* 这些逻辑符号同样可以使用下面的字母替代, 在配置文件中可能会用到
           and (&&), or (||), not (!) */

        ExpressionParser parser = new SpelExpressionParser();
        // false
        Boolean and =  parser.parseExpression("true and false").getValue(Boolean.class);
        // true
        Boolean or =  parser.parseExpression("true || false").getValue(Boolean.class);
        // true
        Boolean not =  parser.parseExpression("!false").getValue(Boolean.class);

        log.debug("The and is [{}]", and);
        log.debug("The or is [{}]", or);
        log.debug("The not is [{}]", not);
    }

    @Test
    public void testTypes()
    {
        /* 我们可以在 SpEL 中用 T(全类名) 的方式获取对应类的 Class 对象, 也可以用
         * T(全类名).staticMethod() 的方式调用类的静态方法. 如果是在 java.lang
         * 包下面的类, 无需使用全类名, 例如 T(String), T(Integer) */

        ExpressionParser parser = new SpelExpressionParser();
        Class<String> stringClass = parser.parseExpression("T(String)").getValue(Class.class);
        Class<User> userClass = parser.parseExpression("T(com.seamew.spel.User)").getValue(Class.class);
        Person person = parser.parseExpression("T(com.seamew.spel.Person).getPerson()").getValue(Person.class);

        log.debug("The stringClass is [{}]", stringClass);
        log.debug("The userClass is [{}]", userClass);
        log.debug("The result is [{}]", person);
    }

    @Test
    public void testInvokeConstructors()
    {
        /* 在 SpEL 中使用 new 关键字来调用构造器创建对象, 除了 java.lang 包下的类, new 后面要跟全类名 */
        ExpressionParser parser = new SpelExpressionParser();
        String string = parser.parseExpression("new String('hello world')").getValue(String.class);
        Person person = parser.parseExpression("new com.seamew.spel.Person('Jack')").getValue(Person.class);
        log.debug("The string is [{}]", string);
        log.debug("The person is [{}]", person);
    }

    @Test
    public void testVariables()
    {
        /* 在 EvaluationContext 中可以使用 setVariable() 方法申明自定义变量, 在 SpEL 表达式中
         * 可以用 #variableName 的方式来引用自定义的对象
         * 🌟 注意由于使用 SimpleEvaluationContext 时不支持对象方法调用, 所以这里为 person 的
         *    name 属性赋值时直接用等号赋值, 也就是 name = #name 的形式 */

        EvaluationContext ctx = SimpleEvaluationContext.forReadWriteDataBinding().build();
        ExpressionParser parser = new SpelExpressionParser();
        Person person = new Person();
        ctx.setVariable("name", "Jack");
        parser.parseExpression("name = #name").getValue(ctx, person);
        log.debug("The person's name is [{}]", person.getName());
    }

    @Test
    public void testThisAndRootVariable()
    {
        /* 在 SpEL 中使用 #this 指向当前正在被解析的对象, 使用 #root 获取根对象,
           这里正在被解析的对象就是 list 中的 string, 根对象是 buckList */
        ExpressionParser parser = new SpelExpressionParser();
        BuckList buckList = new BuckList();
        buckList.add("travel to UK", "travel To Qingdao", "learn singing");

        // .?[condition] 用来根据条件筛选集合类中的元素
        List destinations = parser.parseExpression("list.?[#this.contains('travel')]")
                .getValue(buckList, List.class);

        // 获取根对象
        Object o = parser.parseExpression("#root").getValue(buckList);
        log.debug("The destinations are [{}]", destinations);
        log.debug("The root variable is [{}]", o);
    }

    @Test
    public void testFunctions() throws NoSuchMethodException
    {
        /* EvaluationContext 中的 setVariable() 方法同样可以帮助我们在 EvaluationContext
         * 中声明自定义函数. 第一步需要先通过反射拿到 Method 对象, 第二步调用 setVariable(methodName, method) */
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext ctx = SimpleEvaluationContext.forReadWriteDataBinding().build();
        Method reverse = StringUtil.class.getDeclaredMethod("reverse", String.class);
        ctx.setVariable("reverseString", reverse);
        String backwards = parser.parseExpression("#reverseString('hello world')").getValue(ctx, String.class);
        log.debug("The backwards is [{}]", backwards);
    }

    @Test
    public void testMyBeanResolver()
    {
        /* 我们可以在 StandardEvaluationContext 中通过 setBeanResolver() 方法
           注册一个自定义的实现了 BeanResolver 接口的对象, 这是一个自定义的 bean
           解析器, 通过 bean 的名字可以获取一个 bean. 注册之后我们可以在 SpEL 表达式
           中使用 @beanName 来告诉 spring 从我们自定义的 beanResolver 中获取一个
           bean. */

        ExpressionParser parser = new SpelExpressionParser();
        // 手动构造一个 StandardEvaluationContext
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        // 向 StandardEvaluationContext 注册 MyBeanResolver
        evaluationContext.setBeanResolver(new MyBeanResolver());
        // 通过 @beanName 获取 user
        User user = parser.parseExpression("@user").getValue(evaluationContext, User.class);
        // 通过 @beanName 获取 buckList
        BuckList buckList = parser.parseExpression("@buckList").getValue(evaluationContext, BuckList.class);


        log.debug("The user is [{}]", user);
        log.debug("The buckList is [{}]", buckList.getList());
    }

    @Test
    public void testBeanFactoryResolver()
    {
        /* 我们可以使用 BeanFactoryResolver 来让 SpEL 能够从 BeanFactory 中获取 bean.
         * 获取 FactoryBean 本身以及获取其创建的对象的方法:
         * 1. 获取 FactoryBean: &factoryBeanName
         * 2. 获取 FactoryBean 创建的 bean: @factoryBeanName */

        // 创建一个 beanFactory 作为 bean 的来源
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        // 向 beanFactory 中注册一个单例对象 personFactoryBean
        factory.registerSingleton("personFactoryBean", new PersonFactoryBean());
        // 创建一个 StandardEvaluationContext
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        // 向 context 中注册一个 beanFactoryResolver
        ctx.setBeanResolver(new BeanFactoryResolver(factory));
        ExpressionParser parser = new SpelExpressionParser();
        // 通过 @factoryBeanName 从 personFactoryBean 获取 person
        Person person = parser.parseExpression("@personFactoryBean").getValue(ctx, Person.class);
        // 通过 &factoryBeanName 获取 personFactoryBean 本身
        PersonFactoryBean personFactoryBean = parser.parseExpression("&personFactoryBean")
                .getValue(ctx, PersonFactoryBean.class);

        log.debug("The person is [{}]", person);
        log.debug("The personFactoryBean is [{}]", personFactoryBean);
    }

    @Test
    public void testGetBeanFromApplicationContext()
    {
        /* 我们可以获取 ApplicationContext 中的 BeanFactory 来创建一个 BeanFactoryResolver,
         * 并把它放入 EvaluationContext 中, 在调用 getValue() 方法时传入这个 EvaluationContext,
         * 就可以通过 @beanName &factoryBeanName 这种方式获取 ApplicationContext 中的 bean */

        // 构建一个 ApplicationContext
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        // 从 ApplicationContext 中获取 BeanFactory
        AutowireCapableBeanFactory factory = ctx.getAutowireCapableBeanFactory();
        // 创建一个 StandardEvaluationContext
        StandardEvaluationContext eCtx = new StandardEvaluationContext();
        // 将上面的 BeanFactory 作为 bean 的来源创建一个 BeanFactoryResolver
        eCtx.setBeanResolver(new BeanFactoryResolver(factory));
        ExpressionParser parser = new SpelExpressionParser();
        // 使用 SpEL 从 BeanFactory 中获取一个 bean
        BuckList buckList = parser.parseExpression("@annotationBuckList").getValue(eCtx, BuckList.class);
        log.debug("The buckList is [{}]", buckList.getList());
    }

    @Test
    public void testTernaryOperator()
    {
        /* SpEL 支持三目运算符 ... ? ... : ... (if ... then ... else ...) */
        ExpressionParser parser = new SpelExpressionParser();
        Check check = new Check();
        Boolean result = parser.parseExpression("getFlag() ? true : false").getValue(check, Boolean.class);
        log.debug("The result is [{}]", result);
    }

    @Test
    public void testElvisOperator()
    {
        /* Elvis 运算符是 Ternary 运算符的简化版. 例如 name != null ? name : "lulu"
         * 可以简化为 name ?: "lulu", 含义是, 如果 name 不为 null 就返回 name 本身,
         * 不然就返回 "lulu", 换句话说, 就是给了 name 一个默认值 */

        User user = new User(null, null);
        ExpressionParser parser = new SpelExpressionParser();
        String result = parser.parseExpression("username ?: 'lulu'").getValue(user, String.class);
        log.debug("The result is [{}]", result);
    }

    @Test
    public void testSafeNavigationOperator()
    {
        /* 在 SpEL 中在可能出现空指针的引用后面加上一个 ? 可以避免空指针异常, 因为如过引用
         * 为空, getValue() 方法会直接返回 null 而不是抛出异常 */

        User user1 = new User("admin", "123");
        User user2 = new User(null, null);

        ExpressionParser parser = new SpelExpressionParser();
        String result1 = parser.parseExpression("username?.substring(0)").getValue(user1, String.class);
        String result2 = parser.parseExpression("username?.substring(0)").getValue(user2, String.class);
        log.debug("The result1 is [{}]", result1);
        log.debug("The result2 is [{}]", result2);
    }

    @Test
    public void testCollectionSelection()
    {
        /* 在 SpEL 中我们可以使用 Iterable.?[condition] 这样的语法来筛选集合类或者数组中
         * 的元素, 获得一个相应的子集. spring 会对集合中的每个 entry 执行我们写的判断逻辑
         * (这里要注意, 当前的集合为数组或者 List 时, 对每个元素执行, 当集合为 Map 时, 对每
         * 个 Entry 对象执行), 如果为 true 就保留, false 就舍弃. 这个语法可以运用于任意数组
         * 和实现了 Iterable 接口或者 Map 接口的集合类.
         *
         * 🌟 [] 方括号中写条件时, 可以直接引用集合中元素的属性, 也可以用 #this 来引用当前正在被
         *    解析的元素.下面的例子中, numbers.?[#this >= 10] 的 #this 引用的是 numbers
         *    这个集合中的 Integer 类型的元素
         *
         * 🌟 .^[] 可以获得第一个满足条件的 entry, .$[] 可以获得最后一个满足条件的 entry */

        CollectionSource source = new CollectionSource();
        ExpressionParser parser = new SpelExpressionParser();

        List filteredNumbers = parser.parseExpression("numbers.?[#this >= 10]")
                .getValue(source, List.class);
        // 这里筛选数组有个坑, 只能筛选出第一个满足条件的
        String[] filteredCities = parser.parseExpression("cities.?[#this.contains('Zhejiang')]")
                .getValue(source, String[].class);
        Map filteredScores = parser.parseExpression("scores.?[value >= 60]")
                .getValue(source, Map.class);
        List firstMatchedNumber = parser.parseExpression("numbers.^[#this >= 10]")
                .getValue(source, List.class);
        // 这里可以筛选出最后一个满足条件的
        String[] lastMatchedCity = parser.parseExpression("cities.$[#this.contains('Zhejiang')]")
                .getValue(source, String[].class);
        Map lastMatchedScore = parser.parseExpression("scores.$[value >= 60]")
                .getValue(source, Map.class);

        log.debug("The filteredNumbers are [{}]", filteredNumbers);
        log.debug("The filteredCities are [{}]", filteredCities);
        log.debug("The filteredScores are [{}]", filteredScores);
        log.debug("The firstMatchedNumber is [{}]", firstMatchedNumber);
        log.debug("The lastMatchedCity is [{}]", lastMatchedCity);
        log.debug("The lastMatchedScore is [{}]", lastMatchedScore);
    }

    @Test
    public void testCollectionProjection()
    {
        /* SpEL 中的 projection (投影) 机制可以让集合类或数组有能力触发子表达式的解析.
         * 在下面的例子中, 使用表达式 #this.![username] (#this 在这里就是 users 这
         * 个根对象) 时, spring 会对 集合类 users 中的每一个 entry 执行 [] 中的子表
         * 达式 username, 也就是说, spring 会取出 users 中每一个 user 的 username
         * 属性, 并且把这些 username 都封装到一个 List 中返回 */

        List<User> users = User.exampleUsers();
        ExpressionParser parser = new SpelExpressionParser();
        List<String> usernames = parser.parseExpression("#this.![username]").getValue(users, List.class);
        // 验证例子中的 #this 与 #root 是同一个对象
        Boolean result = parser.parseExpression("#this == #root").getValue(Boolean.class);
        log.debug("The usernames are [{}]", usernames);
        log.debug("#this == #root ? [{}]", result);
    }

    @Test
    public void testExpressionTemplating()
    {
        /* 使用表达式模板允许我们将 literal text (纯文本表达式) 和若干个 evaluation block (表达式解析块)
         * 混用 (模版就是用特定分隔符包裹起来的表达式, 例如 #{...}, 每一个模板就是一个表达式解析块). spring
         * 在解析含有模板的表达式时, 会将模板中表达式的解析结果与表达式中的纯文本以字符串的形式进行连接来作为结果返回.
         * 向要让 SpEL 有能力解析表达式中的模版, 我们需要向 parseExpression() 方法中传入一个 ParserContext
         * 对象, ParserContext 接口提供了自定义表达式的前缀和后缀分隔符以及指定是否为模板的的能力, 它给
         * parseExpression() 方法提供了解析表达式的上下文, 告诉它如何去解析一个表达式. spring 对它的实现类为
         * TemplateParserContext, 其中定义了模版的前缀为 #{ 后缀为 } */
        ExpressionParser parser = new SpelExpressionParser();
        String result = parser.parseExpression("HELLO #{ 'world'.toUpperCase() }",
                new TemplateParserContext()).getValue(String.class);
        log.debug("The result is [{}]", result);
    }

}