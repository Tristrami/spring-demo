package com.seamew.annotationConfig.environment.properties.propertySource;

import org.springframework.core.env.PropertySource;

import java.util.Properties;

/* 我们可以向 spring 的 environment 中添加 key-value 形式的属性源, 例如 Properties,
 * spring 会把我们的属性源存储在 Environment 的 PropertySources 对象中, 这样我们可以通过
 * Environment 对象的 getProperty("key") 方法获得一个属性值. 有两种方法可以配置我们自己的
 * 属性源:
 *
 * 1. 继承 PropertySource 类, 重写 getProperty(String s) 方法, 其中 s 是 key, 返回值
 *    是 value, 也就是说, 在这个方法中我们要自己实现通过 key 获取属性的逻辑. 注意, 实现类还要
 *    加上构造函数 MyPropertySource(String name) 或 MyPropertySource(String name, T source),
 *    name 是当前我们这个 PropertySource 的名称, T 是属性源的类型
 *
 * 2. 使用 @PropertySource("classpath:/...") 注解直接向 PropertySources 中导入 .properties
 *    文件, 那么可以使用 @Value("${key}") 来获取并注入属性值. 见 HikariConfig
 *
 * 🌟 总共有四种方法可以向容器的 PropertySources 中添加属性源
 *    (1) @ImportResource("classpath:/example.xml"), 这个注解用来加载 xml 中的 beanDefinition,
 *        但可以在 xml 中使用 <context:property-placeholder location="..."/>, 当采用以注解为中心的
 *        配置时, 两者配合可以将 .properties 文件引入 PropertySources 中
 *    (2) @PropertySource("classpath:/...")
 *    (3) <context:property-placeholder location="..."/>
 *    (4) 继承 PropertySource, 手动将自己的 PropertySource 添加到 Environment 中的 PropertySources 中
 *
 * 🌟 可以使用 ${key} 占位符来使用属性源中的属性, 例如 @Value("${username}")
 *
 * 🌟 PropertySources 中使用链表存储者所有的 Properties 对象, 当我们尝试从中获得某个属性时, spring 会
 *    逐个搜索每个 Properties 对象 */

public class MyPropertySource extends PropertySource<Properties>
{
    private Properties props;

    public MyPropertySource(String name)
    {
        super(name);
        props = new Properties();
        props.put("username", "admin");
        props.put("password", "123");
    }

    @Override
    public Object getProperty(String s)
    {
        return props.get(s);
    }
}
