package com.seamew.xmlConfig.dao;

import com.seamew.xmlConfig.entity.Order;

public class OrderDaoImpl
{
    public Order getOrder()
    {
        return new Order(1L, 10D, "2022-3-25");
    }
}
