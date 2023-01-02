package com.seamew.xmlConfig.annotation;

public class Dog extends Pet
{
    @Override
    public String toString()
    {
        return "Dog{" +
                "name='" + getName() + '\'' +
                '}';
    }
}
