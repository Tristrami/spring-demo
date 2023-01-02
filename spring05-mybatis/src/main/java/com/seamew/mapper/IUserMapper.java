package com.seamew.mapper;

import com.seamew.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IUserMapper
{
    @Select("select * from user where id = #{userId};")
    User getUserById(@Param("userId") Long userId);

    List<User> getAllUsers();
}
