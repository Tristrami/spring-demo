package com.seamew.annotationConfig.service;

import com.seamew.annotationConfig.dao.UserDaoImpl;
import com.seamew.annotationConfig.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("userServiceImpl")
// @Scope 默认为 singleton
@Scope("singleton")
public class UserServiceImpl
{
    private UserDaoImpl userDao;

    @Autowired
    public UserServiceImpl(UserDaoImpl userDao)
    {
        this.userDao = userDao;
    }

    public User findUser()
    {
        return userDao.findUser();
    }
}
