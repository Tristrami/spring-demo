package com.seamew.xmlConfig.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

@Slf4j
public class EmailBlockedNotifier implements ApplicationListener<EmailBlockedEvent>
{
    private String notificationAddress;

    public EmailBlockedNotifier()
    {
    }

    public EmailBlockedNotifier(String notificationAddress)
    {
        this.notificationAddress = notificationAddress;
    }

    public String getNotificationAddress()
    {
        return notificationAddress;
    }

    public void setNotificationAddress(String notificationAddress)
    {
        this.notificationAddress = notificationAddress;
    }

    @Override
    public void onApplicationEvent(EmailBlockedEvent event)
    {
        notifyAddress();
    }

    public void notifyAddress()
    {
        log.info("Notifying address [{}]", notificationAddress);
    }
}
