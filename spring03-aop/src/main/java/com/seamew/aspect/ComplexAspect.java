package com.seamew.aspect;

import com.seamew.entity.Order;
import com.seamew.entity.User;
import com.seamew.service.IActivityService;
import com.seamew.service.impl.ActivityServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.weaver.tools.JoinPointMatch;
import org.springframework.stereotype.Component;

@org.springframework.core.annotation.Order(1)
@Aspect
@Component
@Slf4j
public class ComplexAspect
{
    /* 带参数的 advice 方法
     * https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-ataspectj-advice-params */

    /**
     * 1. 在 pointcut 方法中添加参数可以让 pointcut 成为参数的提供者, 为使用这个 pointcut 的 advice 提供参数
     * 2. args(order,user) 表示匹配两个参数的方法
     * 3. pointcut 方法中的参数和连接点方法中的参数绑定
     *    @see org.aspectj.weaver.reflect.ShadowMatchImpl#getPointcutParameters(Object, Object, Object[])
     * 4. 按两者的参数列表的顺序一一对应绑定参数值
     */
    @Pointcut(value = "execution(* com.seamew.service.IOrderService.checkOut(..)) && args(order,user)", argNames = "order,user")
    public void checkOut(Order order, User user) {}

    // execution 表达式可以与 args(argName...) 表达式配合使用, 可以为 advice 方法提供参数
    // 我们还可以用类似的方法, 使用 this(beanName) target(proxyBeanName) @within(annotationName)
    // @target(annotationName) @annotation(annotationName) @args(annotationName) 给切点方法或
    // advice 方法绑定相应参数. (表达式中不是指明类型, 而是指明参数名字)
    // 参数绑定分如下步骤:
    // 1. pointcut 方法中的参数和连接点方法中的参数绑定 : ShadowMatchImpl.getPointcutParameters()
    //    按两者的参数列表的顺序一一对应绑定参数值
    // 2. advice 方法中的参数和 pointcut 方法中的参数绑定 : AbstractAspectJAdvice.argBinding()
    //    两者的参数按照参数名对应绑定参数值

    /**
     *  advice 方法中的参数和 pointcut 方法中的参数绑定
     *  @see org.springframework.aop.aspectj.AbstractAspectJAdvice#argBinding(JoinPoint, JoinPointMatch, Object, Throwable)
     *  两者的参数按照参数名对应绑定参数值
     */
    @Before(value = "checkOut(order,user)", argNames = "order,user")
    public void validateOrder(Order order, User user)
    {
        log.info("Validate order [{}] of user [{}]", order, user);
    }

    @Before("execution(* com.seamew.service.IOrderService.refund(..)) && @annotation(checkPoint)")
    public void checkOrderStatus(CheckPoint checkPoint)
    {
        log.info("Check order status before refund");
        log.info("Get annotation [{}] on method [refund()]", checkPoint);
    }

    // 在调用 pjp.proceed() 时可以传递参数给连接点方法, 传递的参数顺序和数量要和连接点方法的参数列表一一对应
    @Around("execution(* com.seamew.service.IOrderService.placeOrder(..)) && args(price)")
    public Object discount(ProceedingJoinPoint pjp, Double price) throws Throwable
    {
        log.info("Give a discount of 30%");
        price *= 0.7;
        return pjp.proceed(new Object[]{price});
    }

    // 不同 Aspect 中的 advice 如果需要编织到同一个连接点上, 那么 advice 的顺序可以通过在 Aspect 类
    // 上使用注解 @Order 来指定优先级, 数字越低优先级越高, 优先级越高的 advice 越先执行
    @Before("execution(* com.seamew.service.IUserService.register(..))")
    public void beforeAdviceOfRegister()
    {
        log.info("ComplexAspect (Order(1))");
    }


    // 同一 Aspect 中的多个 advice 如果需要编织到同一连接点上, spring 会按照 advice 的类型来决定它们的优先
    // 级, 按类型排序依次为: Around -> Before -> After -> AfterReturning -> AfterThrowing, 但需要注
    // 意, After 类型的 Advice 都会在 AfterReturning 和 AfterThrowing 类型的 advice 之后执行. 如果是
    // 同一 Aspect 中的多个同类型的 advice 需哟啊编织到同一连接点上, spring 没有办法控制他们的执行顺序, 我们
    // 要么把它们合并成一个 advice, 要么把它们放在不同的 Aspect 中
    @Around("execution(* com.seamew.service.IOrderService.confirmReceipt(..))")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable
    {
        log.info("Around advice before confirmReceipt()");
        Object retVal = pjp.proceed();
        log.info("Around advice after confirmReceipt()");
        return retVal;
    }

    @Before("execution(* com.seamew.service.IOrderService.confirmReceipt(..))")
    public void beforeAdvice()
    {
        log.info("Before advice of confirmReceipt()");
    }

    @After("execution(* com.seamew.service.IOrderService.confirmReceipt(..))")
    public void afterAdvice()
    {
        log.info("After advice of confirmReceipt()");
    }

    @AfterReturning("execution(* com.seamew.service.IOrderService.confirmReceipt(..))")
    public void afterReturningAdvice()
    {
        log.info("After returning advice of confirmReceipt()");
    }

    @AfterThrowing("execution(* com.seamew.service.IOrderService.confirmReceipt(..))")
    public void afterThrowingAdvice()
    {
        log.info("After throwing advice of confirmReceipt()");
    }

    /* 声明 Introduction */

    // 为被建议的 OrderServiceImpl 类型的对象引入 IActivityService 接口, 并提供默认实现 ActivityServiceImpl
    // @DeclareParents 中的 value 属性是 AspectJ 的类型匹配表达式
    @DeclareParents(value = "com.seamew.service.IOrderService+", defaultImpl = ActivityServiceImpl.class)
    public static IActivityService activityService;
}
