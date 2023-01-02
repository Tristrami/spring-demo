import com.seamew.formatter.AppConfig;
import com.seamew.formatter.MyModel;
import com.seamew.formatter.Student;
import com.seamew.formatter.StudentFormatter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.FormattingConversionService;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class TestFormatter
{
    @Test
    public void testDateFormatter() throws ParseException
    {
        // 测试 Spring 中的 DateFormatter
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateFormatter formatter = new DateFormatter(pattern);
        // 将 Date 对象按照 pattern 格式化为字符串
        String dateString = formatter.print(new Date(System.currentTimeMillis()), Locale.CHINA);
        // 将格式化的字符串解析为一个 Date 对象
        Date date = formatter.parse(dateString, Locale.CHINA);
        log.debug("The dateString is [{}]", dateString);
        log.debug("The date is [{}]", date);
    }

    @Test
    public void testStudentFormatter() throws ParseException
    {
        // 测试自定义的 StudentFormatter
        StudentFormatter formatter = new StudentFormatter();
        // 将 Student 对象格式化为字符串
        String studentString = formatter.print(new Student(1L, "周璐"), Locale.CHINA);
        // 将格式化的字符串解析为一个 Student 对象
        Student student = formatter.parse(studentString, Locale.CHINA);
        log.debug("The studentString is [{}]", studentString);
        log.debug("The student is [{}]", student);
    }

    @Test
    public void testAnnotationFormatterFactory() throws NoSuchFieldException, ParseException
    {
        Class<MyModel> clazz = MyModel.class;
        Field field = clazz.getDeclaredField("decimal");
        // 获取 MyModel 类中 decimal 字段上的 @NumberFormat 注解
        NumberFormat numberFormat = field.getAnnotation(NumberFormat.class);
        // 构建 NumberFormatAnnotationFormatterFactory, 它可以将数字格式化为货币, 百分比等等其它形式
        NumberFormatAnnotationFormatterFactory factory = new NumberFormatAnnotationFormatterFactory();
        // 根据 decimal 字段上的 @NumberFormatter 注解里指定的 style 获取对应的 Printer
        Printer<Number> printer = factory.getPrinter(numberFormat, Number.class);
        // 根据 decimal 字段上的 @NumberFormatter 注解里指定的 style 获取对应的 Parser
        Parser<Number> parser = factory.getParser(numberFormat, Number.class);
        String currencyText = printer.print(new BigDecimal("258"), Locale.CHINA);
        Number currency = parser.parse(currencyText, Locale.CHINA);
        log.debug("The currencyText is [{} - {}]", currencyText, currencyText.getClass());
        log.debug("The currency is [{} - {}]", currency, currency.getClass());
    }

    @Test
    public void testFormattingConversionService()
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("formatter/formatter.xml");
        FormattingConversionService service = ctx.getBean(FormattingConversionService.class);
        Student student = new Student(1L, "lulu");
        String studentString = service.convert(student, String.class);
        log.debug("The studentString is [{}]", studentString);
    }

    @Test
    public void testCustomDateFormatter()
    {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        FormattingConversionService conversionService = ctx.getBean(FormattingConversionService.class);
        Date date = conversionService.convert("2022/4/15", Date.class);
        log.debug("The localDate is [{}]", date);
    }
}
