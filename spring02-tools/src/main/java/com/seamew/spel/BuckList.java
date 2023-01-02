package com.seamew.spel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuckList
{
    private List<String> list;

    public BuckList()
    {
    }

    public BuckList(String ...plans)
    {
        this.add(plans);
    }

    public BuckList(List<String> list)
    {
        this.list = list;
    }

    public List<String> getList()
    {
        return list;
    }

    public void setList(List<String> list)
    {
        this.list = list;
    }

    public void add(String ...plans)
    {
        if (this.list == null)
        {
            this.list = new ArrayList<>();
        }
        list.addAll(Arrays.asList(plans));
    }
}
