package com.seamew.aspect;

import com.seamew.entity.Order;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlAspect
{
    public void myPointcut(Order order) {}

    public void checkOrderStatus(Order order)
    {
        log.info("Check status for order [{}]", order);
    }
}
