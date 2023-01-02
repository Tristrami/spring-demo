import com.seamew.aspect.AppConfig;
import com.seamew.entity.Order;
import com.seamew.entity.User;
import com.seamew.service.IActivityService;
import com.seamew.service.IOrderService;
import com.seamew.service.IUserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestAop
{
    @Test
    public void testBeforeAdvice()
    {
        // 测试 register() 方法上的 BeforeAdvice
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IUserService userService = ctx.getBean(IUserService.class);
        userService.register(new User("admin", "123"));
    }

    @Test
    public void testAfterReturningAdvice()
    {
        // 测试 register() 方法上的 AfterReturningAdvice
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IUserService userService = ctx.getBean(IUserService.class);
        userService.register(new User("admin", "123"));
    }

    @Test
    public void testAfterThrowingAdvice()
    {
        // 测试 login() 方法上的 AfterThrowingAdvice
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IUserService userService = ctx.getBean(IUserService.class);
        try
        {
            userService.login(new User("admin", "123"));
        }
        catch (Exception e)
        {

        }
    }

    @Test
    public void testAfterAdvice()
    {
        // 测试 register() 方法上的 AfterAdvice
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IUserService userService = ctx.getBean(IUserService.class);
        userService.register(new User("admin", "123"));
    }

    @Test
    public void testAroundAdvice()
    {
        // 测试 modifyProfile() 方法上的 AroundAdvice
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IUserService userService = ctx.getBean(IUserService.class);
        userService.modifyProfile(new User("admin", "123"));
    }

    @Test
    public void testAdviceWithMethodParameter()
    {
        // 测试 checkOut() 方法上携带连接点方法参数的 BeforeAdvice
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IOrderService orderService = ctx.getBean(IOrderService.class);
        orderService.checkOut(new Order("candy"), new User("lulu", "123"));
    }

    @Test
    public void testAdviceWithAnnotationParameter()
    {
        // 测试 refund() 方法上携带连接点方法注解的 BeforeAdvice
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IOrderService orderService = ctx.getBean(IOrderService.class);
        orderService.refund(new Order("candy"), new User("lulu", "123"));
    }

    @Test
    public void testProceedingWithArgument()
    {
        // 测试在 AroundAdvice 中修改传递到 placeOrder() 方法中的参数
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IOrderService orderService = ctx.getBean(IOrderService.class);
        orderService.placeOrder(10D);
    }

    @Test
    public void testOrderingOfAdvicesInDifferentAspect()
    {
        // 测试不同 Aspect 中同一连接点上的 advice 的排序
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IUserService userService = ctx.getBean(IUserService.class);
        userService.register(new User("admin", "123"));
    }

    @Test
    public void testOrderingOfAdvicesInTheSameAspect()
    {
        // 测试同一 Aspect 中同一连接点上不同类型的 advice 的排序
        // 这里 confirmReceipt() 方法为抛出异常, 所以 AfterThrowingAdvice 无效
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IOrderService orderService = ctx.getBean(IOrderService.class);
        orderService.confirmReceipt(new Order("candy"));
    }

    @Test
    public void testIntroduction()
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IActivityService orderService = ctx.getBean(IActivityService.class);
        ((IActivityService) orderService).moneyOff();
        ((IOrderService) orderService).checkOut(new Order("candy"), new User("admin", "123"));
    }

    @Test
    public void testXmlAspect()
    {
        // 基于 xml 配置的 aop 配置
        ApplicationContext ctx = new ClassPathXmlApplicationContext("aop.xml");
        IOrderService orderService = ctx.getBean(IOrderService.class);
        orderService.cancelOrder(new Order("candy"));
    }

    @Test
    public void testAdvisor()
    {
        // 测试自定义的 advisor
        ApplicationContext ctx = new ClassPathXmlApplicationContext("advisor.xml");
        IOrderService orderService = ctx.getBean(IOrderService.class);
        orderService.cancelOrder(new Order("candy"));
    }

    @Test
    public void testXmlIntroduction()
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("aop.xml");
        IActivityService orderService = ctx.getBean(IActivityService.class);
        ((IActivityService) orderService).moneyOff();
        ((IOrderService) orderService).checkOut(new Order("candy"), new User("admin", "123"));
    }
}