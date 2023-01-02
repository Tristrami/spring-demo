package com.seamew.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@PropertySource("classpath:hikari.properties")
public class HikariDataSourceConfig
{
    @Value("${jdbcUrl}")
    private String jdbcUrl;

    @Value("${databaseUsername}")
    private String username;

    @Value("${password}")
    private String password;

    @Bean
    public DataSource hikariDataSource()
    {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }
}
