package com.seamew.annotationConfig.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Spanner
{
    @Value("10")
    private int size;

    @Override
    public String toString()
    {
        return "Spanner{" +
                "size=" + size +
                '}';
    }
}
