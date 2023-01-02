import com.seamew.propertyEditor.School;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.propertyeditors.ClassEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class TestPropertyEditor
{
    @Test
    public void testClassEditor()
    {
        // 构造一个 ClassEditor
        ClassEditor editor = new ClassEditor();
        // 将 Person 类的全限定类名解析转化为 Class 对象赋值给 editor 中的 value
        editor.setAsText("com.seamew.propertyEditor.Person");
        // 获取 editor 中的 value
        Class<?> clazz = (Class<?>) editor.getValue();
        // 获取 value 的文本化字符串, 这里是 Person 的全限定类名
        String className = editor.getAsText();
        log.debug("The class is [{}]", clazz);
        log.debug("The className is [{}]", className);
    }

    @Test
    public void testResourceEditor()
    {
        // 构造一个 ResourceEditor
        ResourceEditor editor = new ResourceEditor();
        // 将 hello.txt 的路径名称解析转化为 Resource 对象赋值给 editor 中的 value
        editor.setAsText("classpath:hello.txt");
        // 获取 editor 中的 value, 这里 value 就是 hello.txt 对应的 Resource 对象
        Resource resource = (Resource) editor.getValue();
        log.debug("The resource is [{}]", resource);
    }

    @Test
    public void testCustomDateEditor()
    {
        // 创建一个日期格式对象 dateFormat
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 构造一个 CustomDateEditor
        CustomDateEditor editor = new CustomDateEditor(dateFormat, false);
        // 将日期字符串解析转化为 Date 对象赋值给 editor 中的 value
        editor.setAsText("2022-4-11");
        // 从 editor 中获取 value, 这里就是 Date 对象
        Date date = (Date) editor.getValue();
        // 获取获取 value 的文本化字符串, 这里是使用 dateFormat.format(date) 转化的时间字符串
        String dateString = editor.getAsText();
        log.debug("The date is [{}]", date);
        log.debug("The dateString is [{}]", dateString);
    }

    @Test
    public void testPersonEditor()
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("propertyEditor/property-editor.xml");
        School school = ctx.getBean(School.class);
        log.debug("The school is [{}]", school);
    }
}
