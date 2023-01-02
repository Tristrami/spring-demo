package com.seamew.xmlConfig.service;

import com.seamew.xmlConfig.dao.UserDaoImpl;
import com.seamew.xmlConfig.entity.User;

public class UserServiceImpl
{
    private UserDaoImpl userDao;

    public UserServiceImpl()
    {
    }

    public UserServiceImpl(UserDaoImpl userDao)
    {
        this.userDao = userDao;
    }

    public User getUser()
    {
        return userDao.getUser();
    }

    public void setUserDao(UserDaoImpl userDao)
    {
        this.userDao = userDao;
    }
}
