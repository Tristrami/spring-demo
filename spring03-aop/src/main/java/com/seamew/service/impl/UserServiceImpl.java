package com.seamew.service.impl;

import com.seamew.dao.IUserDao;
import com.seamew.entity.User;
import com.seamew.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Slf4j
@Service
public class UserServiceImpl implements IUserService
{
    @Autowired
    private IUserDao userDao;

    @Override
    public int register(User user)
    {
        try
        {
            userDao.saveUser(user);
        }
        catch (SQLException e)
        {
            log.error("Fail to register user [{}]", user);
        }
        return 1;
    }

    @Override
    public void login(User user)
    {
        int i = 1 / 0;
    }

    @Override
    public int modifyProfile(User user)
    {
        log.info("Modify profile of user [{}]", user);
        return 1;
    }
}
