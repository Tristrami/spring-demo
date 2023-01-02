package com.seamew.resource;

import org.springframework.core.io.Resource;

public class XmlConfigBean
{
    private Resource template;

    public XmlConfigBean(Resource template)
    {
        this.template = template;
    }

    public Resource getTemplate()
    {
        return template;
    }

    public void setTemplate(Resource template)
    {
        this.template = template;
    }
}
