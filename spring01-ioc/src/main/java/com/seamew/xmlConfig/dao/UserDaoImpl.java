package com.seamew.xmlConfig.dao;

import com.seamew.xmlConfig.entity.User;

public class UserDaoImpl
{
    public User getUser()
    {
        return new User(1L, "admin", "123");
    }
}
