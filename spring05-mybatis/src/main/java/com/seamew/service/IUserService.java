package com.seamew.service;

import com.seamew.entity.User;

import java.util.List;

public interface IUserService
{
    User findUserById(Long id);


    List<User> findAllUsers();
}
