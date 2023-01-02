package com.seamew.annotationConfig.component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Toolbox
{
    private String name;
    private Screwdriver screwdriver;
    private Spanner spanner;

    public Toolbox(String name, Screwdriver screwdriver)
    {
        this.screwdriver = screwdriver;
    }

    public Toolbox(String name, Screwdriver screwdriver, Spanner spanner)
    {
        this.name = name;
        this.screwdriver = screwdriver;
        this.spanner = spanner;
    }

    public void init()
    {
        log.debug("toolBox init");
    }

    public void destroy()
    {
        log.debug("toolBox destroy");
    }

    @Override
    public String toString()
    {
        return "ToolBox{" +
                "name='" + name + '\'' +
                ", screwdriver=" + screwdriver +
                ", spanner=" + spanner +
                '}';
    }
}
