package com.seamew.xmlConfig.factory;

import com.seamew.xmlConfig.dao.UserDaoImpl;
import com.seamew.xmlConfig.service.UserServiceImpl;

public class UserServiceFactory
{
    private static final UserServiceImpl USER_SERVICE = new UserServiceImpl(new UserDaoImpl());

    public UserServiceImpl getUserService()
    {
        return USER_SERVICE;
    }
}
