package com.seamew.xmlConfig.service;

import com.seamew.xmlConfig.dao.OrderDaoImpl;
import com.seamew.xmlConfig.dao.ProductDaoImpl;
import com.seamew.xmlConfig.entity.Order;

public class OrderServiceImpl
{
    private OrderDaoImpl orderDao;
    private ProductDaoImpl productDao;

    public OrderServiceImpl(OrderDaoImpl orderDao, ProductDaoImpl productDao)
    {
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    public Order getOrder()
    {
        return orderDao.getOrder();
    }
}
