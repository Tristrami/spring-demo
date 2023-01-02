package com.seamew.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.seamew")
// 使用 @Transactional 注解来配置事物时需要在配置类上加上 @EnableTransactionManagement 注解
// https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction-declarative-annotations
// 事物传播: https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#tx-propagation
@EnableTransactionManagement
@ComponentScan("com.seamew")
public class AppConfig
{
    @Bean
    public JdbcTemplate jdbcTemplate(@Autowired DataSource ds)
    {
        return new JdbcTemplate(ds);
    }

    @Bean("txManager")
    public DataSourceTransactionManager dataSourceTransactionManager(@Autowired DataSource ds)
    {
        return new DataSourceTransactionManager(ds);
    }

    @Bean
    public TransactionTemplate transactionTemplate(@Autowired PlatformTransactionManager txManager)
    {
        return new TransactionTemplate(txManager);
    }
}