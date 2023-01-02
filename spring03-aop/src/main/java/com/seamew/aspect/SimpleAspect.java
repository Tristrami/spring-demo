package com.seamew.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/* 如果一个类中的某些方法拥有针对某个我们的某个关注点来对一类方法进行增强扩展的功能, 我们就可
 * 以将这个类定义为一个切面. 例如一个 ConcurrentOperationExecutor 需要对所有的 service
 * 方法进行一些并发的处理, 那么 ConcurrentOperationExecutor 就可以定义为一个切面, 在其中
 * 利用切点表达式定义相应的 advice 方法对 service 方法做并发相关的扩展处理. 上面提到的"一类
 * 方法" 可以是完成同类型需求的一类方法, 例如 service 方法, 也可以是在同一作用域中的一组方法,
 * 例如 IUserDao 中的方法, 还可以是满足某一种特定条件 (上下文) 的一类方法 , 例如方法所在类
 * 都是某个类的实例, 方法的参数是某个类的实例, 方法上有某种类型的注解等等 */

@Slf4j
@Order(2)
@Aspect
@Component
public class SimpleAspect
{
    @Before("execution(* com.seamew.service.impl.UserServiceImpl.register(..))")
    public void beforeAdviceOfRegister()
    {
        log.info("SimpleAspect (Order(2))");
    }

    /* 我们可以把切点匹配的方法的返回值绑定到 advice 方法的某个参数上, 这样就可以在 advice
     * 方法中使用返回值. 通过 returning 来指定要绑定到 advice 方法中的参数名称. 需要注意,
     * 当我们指定了 returning 属性时, 相当于潜在的增加了一个连接点匹配条件, 也就是需要匹配
     * 有返回值的方法 */
    @AfterReturning(
        pointcut = "execution(* com.seamew.service.impl.UserServiceImpl.register(..))",
        returning = "returnValue")
    public void afterReturningAdvice(Object returnValue)
    {
        log.info("afterReturningAdvice() -- the return value is [{}]", returnValue);
    }

    /* 我们可以把切点匹配的方法抛出的异常绑定到 advice 方法的某个参数上, 这样就可以在 advice
     * 方法中使用这个异常对象. 需要注意, 当我们指定了 throwing 属性时, 相当于潜在的增加了一个
     * 连接点匹配条件, 也就是需要匹配有返回值的方法 */
    @AfterThrowing(
        pointcut = "execution(* com.seamew.service.impl.UserServiceImpl.login(..))",
        throwing = "e"
    )
    public void afterThrowingAdvice(Exception e)
    {
        log.info("afterThrowingAdvice() -- the exception is:", e);
    }


    /* AfterAdvice 方法无论是正常退出还是抛出异常后退出都会被执行 */
    @After("execution(* com.seamew.service.impl.UserServiceImpl.register(..))")
    public void afterAdvice()
    {
        log.info("afterAdvice()");
    }

    /* 对被匹配到的方法的调用最终会返回 AroundAdvice 方法的返回值, 如果AroundAdvice
     * 方法的返回值为 void, 那么原方法的调用者将会始终得到 null */
    @Around("execution(* com.seamew.service.impl.UserServiceImpl.modifyProfile(..))")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable
    {
        log.info("aroundAdvice() -- before");
        Object retVal = pjp.proceed();
        log.info("aroundAdvice() -- after -- the return value is [{}]", retVal);
        return retVal;
    }
}