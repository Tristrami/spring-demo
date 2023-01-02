package com.seamew.annotationConfig.environment.properties.propertySource;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/hikari.properties")
@Configuration("hikariConfig")
@Data
public class HikariConfig
{
    @Value("${jdbcUrl}")
    private String jdbcUrl;

    @Value("${driverClassName}")
    private String driverClassName;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;
}
