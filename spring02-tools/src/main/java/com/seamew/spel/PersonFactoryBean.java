package com.seamew.spel;

import org.springframework.beans.factory.FactoryBean;

import java.util.List;
import java.util.Map;

public class PersonFactoryBean implements FactoryBean<Person>
{
    @Override
    public Person getObject() throws Exception
    {
        Birthday birthday = new Birthday("2000", "11", "21");
        String[] hobbies = new String[]{"ping-pong", "badminton", "coding"};
        List<String> phoneNumber = List.of("15902685501", "19857461899");
        Map<String, String> contacts = Map.of("lulu", "18768592825");
        return new Person("xiaolan", birthday, 21, hobbies, phoneNumber, contacts);
    }

    @Override
    public Class<?> getObjectType()
    {
        return Person.class;
    }
}
