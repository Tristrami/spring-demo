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

        // AppConfig ????????? @Import ????????? HikariConfig ?????? beanDefinition,
        // HikariConfig ????????? @Bean ??????????????????????????? HikariDataSource
        DataSource ds = ctx.getBean(DataSource.class);
        log.debug("The dataSource is [{}]", ds);
    }

    @Test
    public void testFullMode()
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        // ?????? A ??? @Bean ????????????????????? B ??? @Bean ??????, ??????????????? B ??????,
        // ?????????????????? spring ?????????, spring ?????? A ?????????????????? B ???????????????,
        // ????????? B ????????? init-method = init
        A a = ctx.getBean(A.class);
        log.debug("The bean is [{}]", a);
    }

    @Test
    public void testFactoryBean()
    {
        // ?????? DataSourceFactory ??????????????????????????? DataSource
        ApplicationContext ctx = new AnnotationConfigApplicationContext(DataSourceFactoryBean.class);
        Object dataSource = ctx.getBean("dataSourceFactoryBean");
        Object factoryBean = ctx.getBean("&dataSourceFactoryBean");
        log.debug("The dataSource is [{}]", dataSource);
        log.debug("The factoryBean is [{}]", factoryBean);
    }

    @Test
    public void testComponentScanFilter()
    {
        // ExcludeBean ????????? @Filter ??????, ???????????????????????????
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
        // ???????????? "development", ??? "development" ????????????????????????
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
        // ???????????? "product", ??? "product" ????????????????????????
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
        // ?????? jvm ???????????????????????? "test", ???????????????????????? Junit ????????????
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
        // ????????? PropertySources ???????????????????????? PropertySource
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
        // HikariConfig ????????? @PropertySource ??????????????????????????????
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(HikariConfig.class);
        HikariConfig config = ctx.getBean(HikariConfig.class);
        log.debug("The config is [{}]", config);
    }

    @Test
    public void testSpringStandardEvent()
    {
        // ?????? ContextRefreshedEvent ??? ContextClosedEvent ??????
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(StandardEventConfig.class);
        ctx.close();
    }

    @Test
    public void testCustomizedEvent()
    {
        /* UserBlockEvent ??????????????? UserWarningEvent ??????
           UserBlockedListener ??????????????? UserBlockedNotifier, spring ????????????
           UserBlockedListener ?????? @EventListener ?????? */
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(CustomizedEventConfig.class);
        UserService userService = ctx.getBean(UserService.class);
        userService.login(new com.seamew.annotationConfig.event.customized.User("hacker", "123"));
    }
}
