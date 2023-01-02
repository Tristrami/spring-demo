import com.seamew.config.AppConfig;
import com.seamew.entity.User;
import com.seamew.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

@Slf4j
public class TestIUserService
{
    @Test
    public void testFindUserById()
    {
        // 使用注解配置 mapper
        Long userId = 1L;
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IUserService userService = ctx.getBean(IUserService.class);
        User user = userService.findUserById(userId);
        log.debug("The user is [{}]", user);
    }

    @Test
    public void testFindAllUsers()
    {
        // 使用配置文件 mapper
        ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
        IUserService userService = ctx.getBean(IUserService.class);
        List<User> users = userService.findAllUsers();
        log.debug("The users are [{}]", users);
    }
}
