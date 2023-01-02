package com.seamew.spel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/* 在 spring 注解中可以直接使用 SpEL 表达式, 格式为 "#{...}" */

@Configuration
public class AppConfig
{
    @Bean("annotationBuckList")
    public BuckList buckList(@Value("#{ 'travel,singing,reading'.split(',') }") List<String> list)
    {
        return new BuckList(list);
    }
}
