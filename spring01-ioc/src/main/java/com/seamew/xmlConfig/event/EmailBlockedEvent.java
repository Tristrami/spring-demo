package com.seamew.xmlConfig.event;

import org.springframework.context.ApplicationEvent;

public class EmailBlockedEvent extends ApplicationEvent
{
    private String address;
    private String content;

    public EmailBlockedEvent(Object source, String address, String content)
    {
        super(source);
        this.address = address;
        this.content = content;
    }
}
