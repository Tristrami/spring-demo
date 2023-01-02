package com.seamew.annotationConfig.environment.profile.dataSource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("product")
public class ProductDataSourceConfig
{
    @Bean("productDataSource")
    public DataSource getDataSource()
    {
        return new DruidDataSource();
    }
}