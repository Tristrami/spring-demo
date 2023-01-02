package com.seamew.annotationConfig.dao;

import com.seamew.annotationConfig.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("userDaoImpl")
@Scope("singleton")
public class UserDaoImpl
{
    public User findUser()
    {
        return new User("admin");
    }
}
