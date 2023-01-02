package com.seamew.service.impl;

import com.seamew.aspect.CheckPoint;
import com.seamew.entity.Order;
import com.seamew.entity.User;
import com.seamew.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService
{
    @Override
    public void checkOut(Order order, User user)
    {
        log.info("Check out for order [{}] of user [{}]", order, user);
    }

    @Override
    @CheckPoint
    public void refund(Order order, User user)
    {
        log.info("Refund order [{}] of user [{}]", order, user);
    }

    @Override
    public void cancelOrder(Order order)
    {
        log.info("Cancel order [{}]", order);
    }

    @Override
    public void placeOrder(Double price)
    {
        log.info("Place order in price of [{}]", price);
    }

    @Override
    public void confirmReceipt(Order order)
    {
        log.info("Order [{}] confirm receipt", order);
    }
}