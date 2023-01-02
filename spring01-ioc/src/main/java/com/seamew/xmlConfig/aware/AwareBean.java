package com.seamew.xmlConfig.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class AwareBean implements ApplicationContextAware, BeanNameAware
{
    private String beanName;
    private ApplicationContext sourceContext;

    public String getBeanName()
    {
        return beanName;
    }

    public ApplicationContext getSourceContext()
    {
        return sourceContext;
    }

    @Override
    public void setBeanName(String name)
    {
        this.beanName = name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.sourceContext = applicationContext;
    }
}
