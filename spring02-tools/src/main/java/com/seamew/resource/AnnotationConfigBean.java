package com.seamew.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class AnnotationConfigBean
{
    private Resource template;

    // 在 AppConfig 中已经把 classpath:resource/path.properties 加入到了
    // PropertySource 中
    public AnnotationConfigBean(@Value("${templatePath}") Resource template)
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
