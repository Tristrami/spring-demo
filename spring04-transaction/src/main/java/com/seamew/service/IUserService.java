package com.seamew.service;

public interface IUserService
{
    void modifyPassword(String username, String newPassword);


    void modifyUsername(Long userId, String newUsername);
}
