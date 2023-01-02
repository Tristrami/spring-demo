package com.seamew.xmlConfig.circularDependency;

public class D
{
    private C c;

    public D(C c)
    {
        this.c = c;
    }
}
