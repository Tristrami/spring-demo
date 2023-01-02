package com.seamew.annotationConfig.environment.profile.dataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("test")
public class TestDataSourceConfig
{
    @Bean("testDataSource")
    public DataSource getDataSource()
    {
        return new HikariDataSource();
    }
}
