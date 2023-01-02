package com.seamew.service;

import com.seamew.entity.User;

public interface IUserService
{
    int register(User user);


    void login(User user);


    int modifyProfile(User user);
}
