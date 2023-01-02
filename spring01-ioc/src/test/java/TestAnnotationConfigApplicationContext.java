import com.seamew.annotationConfig.configuration.AppConfig;
import com.seamew.annotationConfig.component.Toolbox;
import com.seamew.annotationConfig.entity.A;
import com.seamew.annotationConfig.entity.User;
import com.seamew.annotationConfig.environment.profile.dataSource.DevelopmentDataSourceConfig;
import com.seamew.annotationConfig.environment.properties.propertySource.HikariConfig;
import com.seamew.annotationConfig.environment.properties.propertySource.MyPropertySource;
import com.seamew.annotationConfig.environment.profile.dataSource.ProductDataSourceConfig;
import com.seamew.annotationConfig.environment.profile.dataSource.TestDataSourceConfig;
import com.seamew.annotationConfig.event.customized.CustomizedEventConfig;
import com.seamew.annotationConfig.event.standard.StandardEventConfig;
import com.seamew.annotationConfig.event.customized.UserService;
import com.seamew.annotationConfig.exclude.ExcludeBean;
import com.seamew.annotationConfig.factoryBean.DataSourceFactoryBean;
import com.seamew.annotationConfig.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.*;

import javax.sql.DataSource;

@Slf4j
public class TestAnnotationConfigApplicationContext
{
    @Test
    public void testGetBean()
    {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        Toolbox toolboxWithFullTools = (Toolbox) ctx.getBean("toolbox-full-tools");
        Toolbox toolboxWithScrewdriverOnly = (Toolbox) ctx.getBean("toolbox-screwdriver-only");
        log.debug("The toolBox is [{}]", toolboxWithFullTools);
        log.debug("The toolBox is [{}]", toolboxWithScrewdriverOnly);
        ctx.close();
    }

    @Test
    public void testRepositoryAnnotation()
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        UserServiceImpl userService = ctx.getBean(UserServiceImpl.class);
        User user = userService.findUser();
        log.debug("The user is [{}]", user);
    }

    @Test
    public void testGetDataSource()
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        // AppConfig 中使用 @Import 引入了 HikariConfig 中的 beanDefinition,
        // HikariConfig 中通过 @Bean 向容器中注入了一个 HikariDataSource
        DataSource ds = ctx.getBean(DataSource.class);
        log.debug("The dataSource is [{}]", ds);
    }

    @Test
    public void testFullMode()
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        // 创建 A 的 @Bean 方法会调用创建 B 的 @Bean 方法, 来获得一个 B 对象,
        // 这个调用会被 spring 重定向, spring 会为 A 在容器中寻找 B 对象并注入,
        // 这里为 B 配置了 init-method = init
        A a = ctx.getBean(A.class);
        log.debug("The bean is [{}]", a);
    }

    @Test
    public void testFactoryBean()
    {
        // 获取 DataSourceFactory 本身和它创建出来的 DataSource
        ApplicationContext ctx = new AnnotationConfigApplicationContext(DataSourceFactoryBean.class);
        Object dataSource = ctx.getBean("dataSourceFactoryBean");
        Object factoryBean = ctx.getBean("&dataSourceFactoryBean");
        log.debug("The dataSource is [{}]", dataSource);
        log.debug("The factoryBean is [{}]", factoryBean);
    }

    @Test
    public void testComponentScanFilter()
    {
        // ExcludeBean 被使用 @Filter 排除, 自动扫包不会创建它
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        try
        {
            ctx.getBean(ExcludeBean.class);
        }
        catch (NoSuchBeanDefinitionException e)
        {
            log.error("The bean is excluded", e);
        }
    }

    @Test
    public void testDevelopmentEnvironmentProfile()
    {
        // 激活环境 "development", 从 "development" 环境中获取数据源
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles("development");
        ctx.register(DevelopmentDataSourceConfig.class, ProductDataSourceConfig.class);
        ctx.refresh();
        DataSource ds = ctx.getBean(DataSource.class);
        log.debug("The development dataSource is [{}]", ds);
    }

    @Test
    public void testProductEnvironmentProfile()
    {
        // 激活环境 "product", 从 "product" 环境中获取数据源
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles("product");
        ctx.register(DevelopmentDataSourceConfig.class, ProductDataSourceConfig.class);
        ctx.refresh();
        DataSource ds = ctx.getBean(DataSource.class);
        log.debug("The development dataSource is [{}]", ds);
    }

    @Test
    public void testTestEnvironmentProfile()
    {
        // 使用 jvm 启动参数激活环境 "test", 注意更改右上角的 Junit 构建配置
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
                DevelopmentDataSourceConfig.class,
                ProductDataSourceConfig.class,
                TestDataSourceConfig.class);
        DataSource ds = (DataSource) ctx.getBean("testDataSource");
        log.debug("The test dataSource is [{}]", ds);
    }

    @Test
    public void testMyPropertySource()
    {
        // 手动向 PropertySources 中添加自己实现的 PropertySource
        GenericApplicationContext ctx = new GenericApplicationContext();
        MyPropertySource source = new MyPropertySource("my-property");
        ConfigurableEnvironment env = ctx.getEnvironment();
        MutablePropertySources sources = env.getPropertySources();
        sources.addFirst(source);
        log.debug("The property is [username -> {}]", env.getProperty("username"));
    }

    @Test
    public void testHikariConfig()
    {
        // HikariConfig 使用了 @PropertySource 来向环境中添加属性源
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(HikariConfig.class);
        HikariConfig config = ctx.getBean(HikariConfig.class);
        log.debug("The config is [{}]", config);
    }

    @Test
    public void testSpringStandardEvent()
    {
        // 测试 ContextRefreshedEvent 和 ContextClosedEvent 事件
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(StandardEventConfig.class);
        ctx.close();
    }

    @Test
    public void testCustomizedEvent()
    {
        /* UserBlockEvent 事件会触发 UserWarningEvent 事件
           UserBlockedListener 优先级高于 UserBlockedNotifier, spring 会先调用
           UserBlockedListener 中的 @EventListener 方法 */
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(CustomizedEventConfig.class);
        UserService userService = ctx.getBean(UserService.class);
        userService.login(new com.seamew.annotationConfig.event.customized.User("hacker", "123"));
    }
}
