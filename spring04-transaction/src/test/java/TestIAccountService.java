import com.seamew.config.AppConfig;
import com.seamew.service.IAccountService;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;

public class TestIAccountService
{
    @Test
    public void testClassPathCtxAccountTransfer()
    {
        Double amount = 100D;
        ApplicationContext ctx = new ClassPathXmlApplicationContext("transaction.xml");
        IAccountService accountService = ctx.getBean(IAccountService.class);
        accountService.transfer("xiaolan", "lulu", amount);
    }

    @Test
    public void testClassPathCtxAccountTransferThrowingException()
    {
        // 转账中间出现异常回滚
        Double amount = 100D;
        ApplicationContext ctx = new ClassPathXmlApplicationContext("transaction.xml");
        IAccountService accountService = ctx.getBean(IAccountService.class);
        accountService.transferThrowingException("xiaolan", "lulu", amount);
    }

    @Test
    public void testAnnotationCtxAccountTransfer()
    {
        Double amount = 100D;
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IAccountService accountService = ctx.getBean(IAccountService.class);
        accountService.transfer("xiaolan", "lulu", amount);
    }

    @Test
    public void testAnnotationCtxAccountTransferThrowingException()
    {
        // 转账中间出现异常回滚
        Double amount = 100D;
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        IAccountService accountService = ctx.getBean(IAccountService.class);
        accountService.transferThrowingException("xiaolan", "lulu", amount);
    }
}
