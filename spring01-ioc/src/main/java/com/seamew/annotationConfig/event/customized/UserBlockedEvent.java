package com.seamew.annotationConfig.event.customized;

public class UserBlockedEvent
{
    private String username;

    public UserBlockedEvent(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }
}
