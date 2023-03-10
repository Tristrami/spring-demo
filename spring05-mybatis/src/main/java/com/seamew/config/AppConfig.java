package com.seamew.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.seamew")
@EnableTransactionManagement
public class AppConfig
{
    @Bean
    public PlatformTransactionManager transactionManager(@Autowired DataSource ds)
    {
        return new DataSourceTransactionManager(ds);
    }
}
