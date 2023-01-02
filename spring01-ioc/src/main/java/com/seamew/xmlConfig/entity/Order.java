package com.seamew.xmlConfig.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order
{
    private Long orderId;
    private Double price;
    private String time;
}
