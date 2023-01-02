package com.seamew.entity;

public class Order
{
    private String product;

    public Order()
    {
    }

    public Order(String product)
    {
        this.product = product;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    @Override
    public String toString()
    {
        return "Order{" +
                "product='" + product + '\'' +
                '}';
    }
}
