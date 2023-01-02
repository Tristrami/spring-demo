package com.seamew.annotationConfig.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Screwdriver
{
    @Value("cross")
    private String type;

    @Override
    public String toString()
    {
        return "Screwdriver{" +
                "type='" + type + '\'' +
                '}';
    }
}
