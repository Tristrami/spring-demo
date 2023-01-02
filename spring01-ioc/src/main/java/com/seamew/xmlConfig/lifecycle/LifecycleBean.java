package com.seamew.xmlConfig.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class LifecycleBean implements InitializingBean, DisposableBean
{
    @PostConstruct
    public void annotationInit()
    {
        System.out.println("LifecycleBean [annotationInit()] - @PostConstruct");
    }

    @PreDestroy
    public void annotationDestroy()
    {
        System.out.println("LifecycleBean [annotationDestroy()] - @PreDestroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        System.out.println("LifecycleBean [afterPropertiesSet()] - afterPropertiesSet");
    }

    @Override
    public void destroy() throws Exception
    {
        System.out.println("LifecycleBean [destroy()] - destroy");
    }

    public void customizedInit()
    {
        System.out.println("LifecycleBean [customizedInit()] - customizedInit");
    }

    public void customizedDestroy()
    {
        System.out.println("LifecycleBean [customizedDestroy()] - customizedDestroy");
    }
}
