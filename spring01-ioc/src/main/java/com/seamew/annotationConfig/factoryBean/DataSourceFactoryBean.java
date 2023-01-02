package com.seamew.annotationConfig.factoryBean;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.FactoryBean;

import javax.sql.DataSource;

/* FactoryBean 本身是一个 factory, 是 spring 为我们提供的一个扩展. 实现了 FactoryBean
 * 接口的工厂生产的 bean 可以交给 spring 容器管理. 我们在生产比较复杂的对象时, 如果觉得用配置
 * 文件配置过于繁琐, 而直接自己用代码实现创建过程更加清晰明了, 可以利用 FactoryBean 来实现我们
 * 自己的对象创建逻辑. FactoryBean 的实现类可以当作一个组件注册到 spring 容器中
 *
 * 🌟 将 FactoryBean 注册到容器: new AnnotationConfigApplicationContext(DataSourceFactoryBean.class)
 *
 * 🌟 如何从 spring 容器中获得 FactoryBean 创建的实例和 FactoryBean 本身:
 *    1. 获取工厂创建的实例: context.getBean("[beanId]"), 要注意 beanId 默认是 FactoryBean 的名称首字母小写
 *    2. 获取 BeanFactory 本身: context.getBean("&[beanId]"), 在 beanId 前加上前缀 & 即可 */

public class DataSourceFactoryBean implements FactoryBean<DataSource>
{
    @Override
    public DataSource getObject()
    {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/learn-spring?charsetEncoding=utf-8&serverTimezone=Asia/Shanghai");
        config.setDriverClassName("com.zaxxer.hikari.HikariDataSource");
        config.setUsername("root");
        config.setPassword("ltr20001121");
        return new HikariDataSource(config);
    }

    @Override
    public Class<?> getObjectType()
    {
        return HikariDataSource.class;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
