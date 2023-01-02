package com.seamew.propertyEditor;

public class School
{
    private Person headMaster;

    public School()
    {
    }

    public School(Person headMaster)
    {
        this.headMaster = headMaster;
    }

    public Person getHeadMaster()
    {
        return headMaster;
    }

    public void setHeadMaster(Person headMaster)
    {
        this.headMaster = headMaster;
    }

    @Override
    public String toString()
    {
        return "School{" +
                "headMaster=" + headMaster +
                '}';
    }
}
