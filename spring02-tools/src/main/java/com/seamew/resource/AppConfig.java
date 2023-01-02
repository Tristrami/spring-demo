package com.seamew.resource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("com.seamew.resource")
@PropertySource("classpath:resource/path.properties")
@Configuration
public class AppConfig
{
}
