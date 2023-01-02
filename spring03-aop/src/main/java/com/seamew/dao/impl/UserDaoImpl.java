package com.seamew.dao.impl;

import com.seamew.dao.IUserDao;
import com.seamew.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Slf4j
@Repository
public class UserDaoImpl implements IUserDao
{
    @Override
    public void saveUser(User user) throws SQLException
    {
        if (user == null)
        {
            throw new SQLException("The user is null");
        }
        else
        {
            log.info("Save user [{}] in database", user);
        }
    }
}
