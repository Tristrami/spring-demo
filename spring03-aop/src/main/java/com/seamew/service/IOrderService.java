package com.seamew.service;

import com.seamew.entity.Order;
import com.seamew.entity.User;

public interface IOrderService
{
    void checkOut(Order order, User user);


    void refund(Order order, User user);


    void cancelOrder(Order order);


    void placeOrder(Double price);


    void confirmReceipt(Order order);
}
