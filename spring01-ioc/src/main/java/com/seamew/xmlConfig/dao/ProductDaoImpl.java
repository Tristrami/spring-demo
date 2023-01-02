package com.seamew.xmlConfig.dao;

import com.seamew.xmlConfig.entity.Product;

public class ProductDaoImpl
{
    public Product getProduct()
    {
        return new Product(1L, "candy", 10D);
    }
}
