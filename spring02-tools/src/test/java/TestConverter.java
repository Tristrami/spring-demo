import com.seamew.converter.Animal;
import com.seamew.converter.PetShop;
import com.seamew.converter.StringToIntegerConverter;
import com.seamew.converter.StringToNumberConverterFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

@Slf4j
public class TestConverter
{
    @Test
    public void testStringToIntegerConverter()
    {
        // 测试 string 到 Integer 的转换器
        StringToIntegerConverter converter = new StringToIntegerConverter();
        String source = "357";
        Integer target = converter.convert(source);
        log.debug("The result is [{}], type is [{}]", target, target.getClass());
    }

    @Test
    public void testStringToNumberConverterFactory()
    {
        // 测试 String 到 Number 的 ConverterFactory
        StringToNumberConverterFactory factory = new StringToNumberConverterFactory();
        Converter<String, Double> converter = factory.getConverter(Double.class);
        String source = "423.24";
        Double target = converter.convert(source);
        log.debug("The result is [{}], type is [{}]", target, target.getClass());
    }

    @Test
    public void testConversionService()
    {
        // 测试自定义的 AnimalConverter, 把 String 转换为 Animal
        ApplicationContext ctx = new ClassPathXmlApplicationContext("converter/converter.xml");
        ConversionService service = (ConversionService) ctx.getBean("conversionServiceFactoryBean");
        String source = "cat";
        Animal target = service.convert(source, Animal.class);
        log.debug("The target is [{}]", target);
    }

    @Test
    public void testDefaultConversionService()
    {
        // 测试 DefaultConversionService 中的 ObjectToStringConverter
        DefaultConversionService service = new DefaultConversionService();
        Animal cat = new Animal("cat");
        String text = service.convert(cat, String.class);
        log.debug("The result is [{}]", text);
    }

    @Test
    public void testPetShop()
    {
        // spring 会使用我们注册的 StringToAnimalConverter 将字符串转换为 Animal对象
        // 注入到 PetShop 的 animal 属性中
        ApplicationContext ctx = new ClassPathXmlApplicationContext("converter/converter.xml");
        PetShop shop = ctx.getBean(PetShop.class);
        log.debug("The petShop is [{}]", shop);
    }
}
