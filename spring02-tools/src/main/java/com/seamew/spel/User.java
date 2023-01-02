package com.seamew.spel;

import java.util.ArrayList;
import java.util.List;

public class User
{
    private String username;
    private String password;

    public User()
    {
    }

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public static List<User> exampleUsers()
    {
        List<User> users = new ArrayList<>(3);
        users.add(new User("admin", "123"));
        users.add(new User("lulu", "456"));
        users.add(new User("xiaolan", "789"));
        return users;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
