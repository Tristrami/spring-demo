package com.seamew.spel;

import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyBeanResolver implements BeanResolver
{
    private static final Map<String, Object> beans;

    static
    {
        beans = new ConcurrentHashMap<>(64);
        beans.put("user", new User("admin", "123"));
        beans.put("buckList", new BuckList("Travel to Qingdao", "Travel to Norway"));
        beans.put("personFactoryBean", new PersonFactoryBean());
    }

    @Override
    public Object resolve(EvaluationContext context, String beanName) throws AccessException
    {
        return beans.get(beanName);
    }
}
