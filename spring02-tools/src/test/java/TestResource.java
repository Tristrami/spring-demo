import com.seamew.resource.AnnotationConfigBean;
import com.seamew.resource.AppConfig;
import com.seamew.resource.XmlConfigBean;
import com.seamew.utils.ResourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;

/* 1. Resource: spring 对资源抽象出的接口
 *    (1) UrlResource: 加载 URL 对应的资源, 底层使用 java.net.URL
 *    (2) ClassPathResource: 加载类路径下的资源, 底层使用 java.io.File
 *    (3) FileSystemResource: 加载文件系统中的资源, 底层使用 java.io.File, java.nio.file.Files
 *    (4) PathResource: 加载文件系统或 URL 资源, 底层使用 java.nio.file.Path, 实现了 WritableResource 接口
 *    (5) ServletContextResource: 从 WEB 应用路径下获取资源
 *    (6) InputStreamResource: 只能用在已将已经被打开的资源上
 *    (7) ByteArrayResource: 从字节数组中获取资源
 *    当我们使用 ctx.getResource() 方法获取资源时, 如果没有指定资源路径的前缀, 那么默认情况下,不同的
 *    ApplicationContext 会返回不同的资源. 例如, ClassPathXmlApplicationContext 默认返回
 *    ClassPathResource, FileSystemXmlApplicationContext 默认返回 FileSystemResource.
 *    我们也可以在资源路径前加上特定的前缀让 ApplicationContext 强制返回某一类型的资源, 例如 file:
 *    classpath: https:
 *
 * 2. ResourceLoader: 资源加载接口
 * 3. ResourcePatternResolver: 资源路径解析器接口, 是对 ResourceLoader 接口的扩展
 * 4. ResourceLoaderAware 接口: 通过实现这个接口可以让我们的类有获取 ResourceLoader 的能力,
 *    实际上, spring 会把 ApplicationContext 作为一个 ResourceLoader 交给我们
 *
 * 🌟 classpath: 和 classpath*: 的区别
 *    使用 classpath: 前缀 spring 只会从当前项目的类路径下加载资源, 而使用 classpath*:
 *    时, spring 会扫描所有的类路径, 并加载所有对应的资源, 也就是说, 除了项目本身的类路径
 *    意外, spring 还会从 JAR 类包或 ZIP 类包的类路径下寻找资源
 *
 * 🌟 ant-style 通配符: 我们可以在 ApplicationContext 的 **构造函数** 中使用 ant-style
 *    资源路径的通配符
 *    (1) ? - 匹配任何单字符
 *    (2) * - 匹配 0 或任意数量的字符
 *    (3) ** - 匹配 0 或 更多个的目录
 *
 * 🌟 当某个 bean 中有 Resource 类型的依赖时, 我们可以直接使用资源路径的字符串作为注入值,
 *    spring 会帮我们解析这个路径, 把这个路径对应的资源注入进去. 见 XmlConfigBean 和
 *    AnnotationConfigBean */

@Slf4j
public class TestResource
{
    @Test
    public void testGetResource()
    {
        // 获取类路径 resource 目录下 hello.txt 中的内容
        ApplicationContext ctx = new ClassPathXmlApplicationContext();
        Resource resource = ctx.getResource("classpath:resource/hello.txt");
        String content = ResourceUtils.getContent(resource);
        log.debug("The content of resource is [{}]", content);
    }

    @Test
    public void testInjectResourceByXmlConfig()
    {
        // 加载类路径中 resource 目录下的所有 .xml 文件
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:resource/**/*.xml");

        // 通过构造器将 Resource 类型的依赖 template 注入 bean 中
        XmlConfigBean bean = ctx.getBean(XmlConfigBean.class);

        Resource template = bean.getTemplate();
        String content = ResourceUtils.getContent(template);
        log.debug("The content of template is [{}]", content);
    }

    @Test
    public void testInjectResourceByAnnotationConfig()
    {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        AnnotationConfigBean bean = ctx.getBean(AnnotationConfigBean.class);

        // 使用 @Value 注入资源
        Resource template  = bean.getTemplate();
        String content = ResourceUtils.getContent(template);
        log.debug("The content of template is [{}]", content);
    }
}
