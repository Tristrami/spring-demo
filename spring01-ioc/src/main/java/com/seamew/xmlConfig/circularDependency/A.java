package com.seamew.xmlConfig.circularDependency;

public class A
{
    private B b;

    public void setB(B b)
    {
        this.b = b;
    }
}
