package com.seamew.beanWrapper;

public class Company
{
    private String name;
    private Employee managingDirector;

    public Company()
    {
    }

    public Company(String name, Employee managingDirector)
    {
        this.name = name;
        this.managingDirector = managingDirector;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Employee getManagingDirector()
    {
        return managingDirector;
    }

    public void setManagingDirector(Employee managingDirector)
    {
        this.managingDirector = managingDirector;
    }

    @Override
    public String toString()
    {
        return "Company{" +
                "name='" + name + '\'' +
                ", managingDirector=" + managingDirector +
                '}';
    }
}
