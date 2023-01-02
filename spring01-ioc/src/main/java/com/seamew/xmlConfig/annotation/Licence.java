package com.seamew.xmlConfig.annotation;

public class Licence
{
    private String name;

    public Licence(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "Licence{" +
                "name='" + name + '\'' +
                '}';
    }
}
