package com.seamew.xmlConfig.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Data
public class Person
{
    private String name;
    private int age;
    private String[] hobbies;
    private List<String> familyMembers;
    private Map<String, String> contact;
    private Properties props;
    private Set<String> phoneNumbers;
    private Address address;

    public Person()
    {
    }

    public Person(String name, int age)
    {
        this.name = name;
        this.age = age;
    }

    public Person(Address address)
    {
        this.address = address;
    }
}
