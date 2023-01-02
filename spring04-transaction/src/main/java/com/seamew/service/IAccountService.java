package com.seamew.service;

public interface IAccountService
{
    void transfer(String from, String to, Double amount);

    void transferThrowingException(String from, String to, Double amount);
}
