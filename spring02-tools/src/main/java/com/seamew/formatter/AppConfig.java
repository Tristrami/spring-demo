package com.seamew.formatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.time.format.DateTimeFormatter;

@Configuration
public class AppConfig
{
    @Bean
    public FormattingConversionService conversionService()
    {
        // 自定义日期 formatter
        DefaultFormattingConversionService service = new DefaultFormattingConversionService(false);
        service.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

        DateTimeFormatterRegistrar dtRegistrar = new DateTimeFormatterRegistrar();
        dtRegistrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        dtRegistrar.registerFormatters(service);

        DateFormatterRegistrar dRegistrar = new DateFormatterRegistrar();
        dRegistrar.setFormatter(new DateFormatter("yyyy/MM/dd"));
        dRegistrar.registerFormatters(service);

        return service;
    }
}
