import com.seamew.config.AppConfig;
import com.seamew.service.IUserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestIUserService
{
    @Test
    public void testModifyPassword()
    {
        String newPassword = "newPassword";
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IUserService userService = ctx.getBean(IUserService.class);
        userService.modifyPassword("lulu", newPassword);
    }

    @Test
    public void testModifyUsername()
    {
        String newUsername = "xiaoyu";
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IUserService userService = ctx.getBean(IUserService.class);
        userService.modifyUsername(2L, newUsername);
    }
}