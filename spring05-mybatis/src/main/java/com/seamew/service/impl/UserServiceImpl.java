package com.seamew.service.impl;

import com.seamew.entity.User;
import com.seamew.mapper.IUserMapper;
import com.seamew.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService
{
    @Autowired
    private IUserMapper userMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User findUserById(Long id)
    {
        return userMapper.getUserById(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> findAllUsers()
    {
        return userMapper.getAllUsers();
    }
}
