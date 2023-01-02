package com.seamew.annotationConfig.event.customized;

public class UserWarningEvent
{
    private String message;

    public UserWarningEvent(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}
