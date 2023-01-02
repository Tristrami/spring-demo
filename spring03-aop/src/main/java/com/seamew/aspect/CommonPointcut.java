package com.seamew.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class CommonPointcut
{
    // 通过指定方法的作用域来匹配方法, 所以用 in...Layer 作为切入点名称来体现这些方法在某一共同作用域中
    @Pointcut("within(com.seamew.dao..*)")
    public void inDataAccessLayer() {}

    @Pointcut("within(com.seamew.service..*)")
    public void inServiceLayer() {}

    // 通过指定方法类型匹配方法, 切入点命名需要体现出是这些方法是某一种类的操作
    @Pointcut("execution(* com.seamew.dao..*.*(..))")
    public void dataAccessOperation() {}

    @Pointcut("execution(* com.seamew.service..*.*(..))")
    public void businessService() {}
}
