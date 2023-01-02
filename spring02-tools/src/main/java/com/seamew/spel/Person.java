package com.seamew.spel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Person
{
    private String name;
    private Birthday birthday;
    private int age;
    private String[] hobbies;
    private List<String> phoneNumber;
    private Map<String, String> contacts;

    public Person()
    {
    }

    public Person(String name)
    {
        this.name = name;
    }

    public Person(String name, Birthday birthday, int age, String[] hobbies, List<String> phoneNumber, Map<String, String> contacts)
    {
        this.name = name;
        this.birthday = birthday;
        this.age = age;
        this.hobbies = hobbies;
        this.phoneNumber = phoneNumber;
        this.contacts = contacts;
    }

    public static Person getPerson()
    {
        return new Person("factoryMethod");
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Birthday getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Birthday birthday)
    {
        this.birthday = birthday;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String[] getHobbies()
    {
        return hobbies;
    }

    public void setHobbies(String[] hobbies)
    {
        this.hobbies = hobbies;
    }

    public List<String> getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(List<String> phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public Map<String, String> getContacts()
    {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts)
    {
        this.contacts = contacts;
    }

    @Override
    public String toString()
    {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", age=" + age +
                ", hobbies=" + Arrays.toString(hobbies) +
                ", phoneNumber=" + phoneNumber +
                ", contacts=" + contacts +
                '}';
    }
}
