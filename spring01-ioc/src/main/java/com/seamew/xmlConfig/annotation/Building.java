package com.seamew.xmlConfig.annotation;

public class Building
{
    private String type;

    public Building(String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "Building{" +
                "type='" + type + '\'' +
                '}';
    }
}
