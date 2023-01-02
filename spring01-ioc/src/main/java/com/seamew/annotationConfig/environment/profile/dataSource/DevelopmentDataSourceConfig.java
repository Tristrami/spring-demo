package com.seamew.annotationConfig.environment.profile.dataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/* spring 为我们提供了多环境配置, 我们可以为特定的 bean 设置特定的环境, 例如这里为
 * DevelopmentDataSourceConfig 这个配置类设置环境为 development, 那么只有在
 * 我们选择激活 development 环境时这个配置类和里面的 bean 才会被注册到容器中
 *
 * 🌟 @Profile 注解中的指定环境的字符串中可以包含 & | ! 运算符, & ｜ 同时使用时,
 *    必须在 | 条件两边加括号. 其中, ! 代表非运算, 比如 "!profile" 代表 "profile"
 *    环境未激活时加载这个 bean, & 代表与运算, ｜ 代表或运算. 一个 profile 是一个
 *    给定名字的, 在逻辑上分了组的 beanDefinition 配置
 *
 * 🌟 激活环境的四种方法:
 *
 *   🔍 通过注解配置多环境时:
 *      // 1. 构造容器
 *      AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
 *      // 2. 为容器设置要激活的环境
 *      ctx.getEnvironment().setActiveProfiles("product");
 *      // 3. 向容器中注册组件
 *      ctx.register(DevelopmentDataSourceConfig.class, ProductDataSourceConfig.class);
 *      // 4. 刷新容器, 解析 beanDefinition
 *      ctx.refresh();
 *
 *   🔍 通过 xml 配置多环境时:
 *      // 1. 构造容器
 *      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("environment.xml");
 *      // 2. 为容器设置要激活的环境
 *      context.getEnvironment().setActiveProfiles("development");
 *      // 3. 刷新容器, 解析 xml 中的 beanDefinition
 *      context.refresh();
 *
 *   🔍 通过 JVM 启动参数激活: -Dspring.profiles.active="profile1,profile2"
 *
 *   🔍 设置默认激活的环境: 设置 profile 名称为 "default" 时 spring 会将这个 profile 识别为默认的环境配置,
 *      或者我们也可以通过下面两种方法指定默认的 profile
 *      (1) context.getEnvironment().setDefaultProfiles("profile1", "profile2")
 *      (2) 使用 Jvm 启动参数指定: spring.profiles.default="profile1,profile2"
 * */

@Configuration
@Profile("development")
public class DevelopmentDataSourceConfig
{
    @Bean("developmentDataSource")
    public DataSource getDataSource()
    {
        return new HikariDataSource();
    }
}
