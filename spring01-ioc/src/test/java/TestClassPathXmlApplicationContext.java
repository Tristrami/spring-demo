import com.seamew.annotationConfig.component.Toolbox;
import com.seamew.xmlConfig.event.EmailService;
import com.seamew.xmlConfig.annotation.PetShop;
import com.seamew.xmlConfig.aware.AwareBean;
import com.seamew.xmlConfig.beanScope.Prototype;
import com.seamew.xmlConfig.beanScope.Singleton;
import com.seamew.xmlConfig.circularDependency.A;
import com.seamew.xmlConfig.circularDependency.B;
import com.seamew.xmlConfig.circularDependency.C;
import com.seamew.xmlConfig.circularDependency.D;
import com.seamew.xmlConfig.dao.UserDaoImpl;
import com.seamew.xmlConfig.entity.LazyBean;
import com.seamew.xmlConfig.entity.Person;
import com.seamew.xmlConfig.lifecycle.LifecycleBean;
import com.seamew.xmlConfig.service.OrderServiceImpl;
import com.seamew.xmlConfig.service.ProductServiceImpl;
import com.seamew.xmlConfig.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

@Slf4j
public class TestClassPathXmlApplicationContext
{
    @Test
    public void testGetBean()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("daos.xml");

        /* 🌟 只要通过 class 获取 bean 必须要保证这个 bean 是单例 */
        UserDaoImpl userDao = (UserDaoImpl) context.getBean("userDao");
        log.debug("The result is [{}]", userDao.getUser());
    }

    @Test
    public void test()
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("daos.xml");
        UserDaoImpl userDao1 = ctx.getBean(UserDaoImpl.class);
        UserDaoImpl userDao2 = ctx.getBean(UserDaoImpl.class);
        log.info("userDao1 == userDao2 ? [{}]", userDao1 == userDao2);
    }

    @Test
    public void testGetBeanByStaticFactoryMethod()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml", "factories.xml");

        /* 在 services.xml 中配置 userService 的 factory-bean 为 userServiceFactory,
         * factory-method 为 getUserService, ioc 容器在初始化 userService 对象时就会去
         * 调用 userServiceFactory 对象的实例方法 getUserService 来构造 userService 对象 */
        UserServiceImpl userService = context.getBean(UserServiceImpl.class, "userService");
        log.debug("The result is [{}]", userService.getUser());
    }

    @Test
    public void testDIByConstructor()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml", "factories.xml");

        /* ioc 容器在构造 orderService 对象时发现它有两个依赖 orderDao 和 productDao, 容器
         * 会先去构造这两个 bean, 然后这里会使用构造器将这两个属性注入 orderService. 发现并构造
         * 依赖的过程类似于深搜, 刚好是我们手动构造依赖但反过程
         * eg. A -> B -> C, -> 表示左边依赖右边, 手动构造的顺序一般是 C, B(C), A(B), () 表示
         * 调用构造函数; ioc 构造的顺序是: 构造 A, 发现依赖 B, 构造 B, 发现依赖 C, 构造 C, C 完成构造,
         *  C 注入 B, B 完成构造, B 注入 A, A 完成构造 */
        OrderServiceImpl orderService = context.getBean(OrderServiceImpl.class, "orderService");
        log.debug("The result is [{}]", orderService.getOrder());
    }

    @Test
    public void testDIBySetter()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml", "factories.xml");

        /* 用 setter 注入 productService 的 productDao 属性来完成构造 */
        ProductServiceImpl productService = context.getBean(ProductServiceImpl.class, "productService");
        log.debug("The result is [{}]", productService.getProduct());
    }

    @Test
    public void testPrimitiveAndCollectionDI()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("di-example.xml");

        /* DI 注入原语类型和集合类型属性, 包含基础数据类型, 数组, String, List, Set, Map, Properties
         * 在 <property> 标签中添加对应标签即可 */
        Person person = (Person) context.getBean("person");
        log.debug("The person is [{}]", person);
    }

    @Test
    public void testNamespaceDI()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("di-example.xml");

        /* pPerson 用 p-namespace 注入, cPerson 用 c-namespace 注入 */
        Person pPerson = (Person) context.getBean("p-namespace-person");
        Person cPerson = (Person) context.getBean("c-namespace-person");
        log.debug("The p-namespace person is [{}]", pPerson);
        log.debug("The c-namespace person is [{}]", cPerson);
    }

    @Test
    public void testLazyInit()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("di-example.xml");

        /* address 这个 bean 被配置为了 lazy-init, 即懒加载模式, ioc 容器初始化时不会加载这个 bean,
         * 只有当使用这个 bean 时, 它才会被加载. 🌟 注意, 如果要保证 bean 一定是懒加载的, 那就一定要保
         * 证这个 bean 没有被其它非懒加载的 bean 依赖 */
        log.debug("Statement before using Address class");
        LazyBean lazyBean = (LazyBean) context.getBean("lazy-bean");
        log.debug("The address is [{}]", lazyBean);
    }

    @Test
    public void testAutoWire()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("di-example.xml");

        // 通过三种方式自动装配 person 的 address 属性
        Person autoWiredByNamePerson = (Person) context.getBean("autowired-byName-person");
        Person autoWiredByTypePerson = (Person) context.getBean("autowired-byType-person");
        Person autoWiredConstructorPerson = (Person) context.getBean("autowired-constructor-person");
        log.debug("The autowired by name person is [{}]", autoWiredByNamePerson);
        log.debug("The autowired by type person is [{}]", autoWiredByTypePerson);
        log.debug("The autowired by constructor person is [{}]", autoWiredConstructorPerson);
    }

    @Test
    public void testResolvableCircularDependency()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("circular-dependency.xml");

        /* a -> b, b -> a, a, b 都是通过 setter 注入, 循环依赖可以通过两级缓存解决,
         * 详见 circular-dependency.xml */
        A a = context.getBean(A.class);
        B b = context.getBean(B.class);
        log.debug("The instance of Type A is [{}]", a);
        log.debug("The instance of Type B is [{}]", b);
    }

    @Test
    public void testUnresolvableCircularDependency()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("circular-dependency.xml");

        /* c -> d, d -> c, 但 c, d 都是通过构造器注入, c, d 都无法被构建, 两级缓存
         * 无法解决循环依赖问题, 详见 circular-dependency.xml */
        try
        {
            C c = context.getBean(C.class);
            D d = context.getBean(D.class);
        }
        catch (UnsatisfiedDependencyException e)
        {
            log.error("Circular reference detected!\nCause:", e);
        }
    }

    @Test
    public void testBeanScope()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean-scope.xml");

        /* s1, s2 在容器中的作用域为 singleton, 所以 s1 和 s2 是同一个对象
         * p1, p2 在容器中的作用域为 prototype, 所以每次请求一个 Prototype
         * 类型的对象时, 容器都会创建一个新的实例, 所以 p1 和 p2 是不同对象 */

        Singleton s1 = (Singleton) context.getBean("singleton");
        Singleton s2 = (Singleton) context.getBean("singleton");
        Prototype p1 = (Prototype) context.getBean("prototype");
        Prototype p2 = (Prototype) context.getBean("prototype");
        log.debug("Singleton s1: [{}], singleton s2: [{}]", s1, s2);
        log.debug("Prototype p1: [{}], prototype p2: [{}]", p1, p2);
    }

    @Test
    public void testLifecycleCallback()
    {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("lifecycle.xml");

        LifecycleBean bean = (LifecycleBean) context.getBean("lifecycle-bean");
        log.debug("The lifecycleBean is [{}]", bean);
        // 关闭容器才会回调 DisposableBean 接口的 destroy 方法
        context.close();
    }

    @Test
    public void testAwareInterface()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("aware.xml");

        // 这个 bean 可以获得创造它的 ApplicationContext 和这个 bean 在容器中的名字
        AwareBean bean = context.getBean(AwareBean.class);
        log.debug("The name of bean in container is [{}]", bean.getBeanName());
        log.debug("The source context is [{}]", bean.getSourceContext());
    }

    @Test
    public void testAutowiredAndResourceAnnotation()
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("annotation.xml");

        // 通过 @Autowired 注入
        PetShop shop = context.getBean(PetShop.class);
        log.debug("The pet shop is [{}]", shop);
    }

    @Test
    public void testImportAppConfig()
    {
        // xml-config.xml 中引入了 AppConfig 中的 beanDefinition
        ApplicationContext context = new ClassPathXmlApplicationContext("xml-config.xml");
        Toolbox toolbox = (Toolbox) context.getBean("toolbox-full-tools");
        log.debug("The toolbox is [{}]", toolbox);
    }

    @Test
    public void testDevelopmentEnvironmentProfile()
    {
        // 激活环境 "development", 从 "development" 环境中获取数据源
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("environment.xml");
        context.getEnvironment().setActiveProfiles("development");
        context.refresh();
        DataSource ds = context.getBean(DataSource.class);
        log.debug("The development dataSource is [{}]", ds);
    }

    @Test
    public void testProductEnvironmentProfile()
    {
        // 激活环境 "product", 从 "product" 环境中获取数据源
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("environment.xml");
        context.getEnvironment().setActiveProfiles("product");
        context.refresh();
        DataSource ds = context.getBean(DataSource.class);
        log.debug("The development dataSource is [{}]", ds);
    }

    @Test
    public void testTestEnvironmentProfile()
    {
        // 使用 jvm 启动参数激活环境 "test", 从 "test" 环境中获取数据源, 注意更改右上角的 Junit 构建配置
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("environment.xml");
        DataSource ds = (DataSource) context.getBean("testDataSource");
        log.debug("The test dataSource is [{}]", ds);
    }

    @Test
    public void testEvent()
    {
        // EmailService 发送邮件时会触发 EmailBlockedEvent 事件, Spring 会通知订阅了
        // 这个事件的 Listener, 也就是 EmailBlockedNotifier
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("email-event.xml");
        EmailService emailService = context.getBean(EmailService.class);
        emailService.sendEmail("example@qq.com", "test");
    }
}
