package com.seamew.xmlConfig.circularDependency;

public class C
{
    private D d;

    public C(D d)
    {
        this.d = d;
    }
}
