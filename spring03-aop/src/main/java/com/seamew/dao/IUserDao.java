package com.seamew.dao;

import com.seamew.entity.User;

import java.sql.SQLException;

public interface IUserDao
{
    void saveUser(User user) throws SQLException;
}
