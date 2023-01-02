package com.seamew.xmlConfig.service;

import com.seamew.xmlConfig.dao.ProductDaoImpl;
import com.seamew.xmlConfig.entity.Product;

public class ProductServiceImpl
{
    private ProductDaoImpl productDao;

    public ProductServiceImpl()
    {
    }

    public ProductServiceImpl(ProductDaoImpl productDao)
    {
        this.productDao = productDao;
    }

    public Product getProduct()
    {
        return productDao.getProduct();
    }

    public void setProductDao(ProductDaoImpl productDao)
    {
        this.productDao = productDao;
    }
}
