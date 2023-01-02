package com.seamew.validator.customized;

import javax.validation.constraints.Size;

public class Person
{
    @Size()
    private String name;

    @PersonConstraint(minAge = 0, maxAge = 170)
    private Integer age;

    public Person()
    {
    }

    public Person(String name, Integer age)
    {
        this.name = name;
        this.age = age;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getAge()
    {
        return age;
    }

    public void setAge(Integer age)
    {
        this.age = age;
    }

    @Override
    public String toString()
    {
        return "PersonModel{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
