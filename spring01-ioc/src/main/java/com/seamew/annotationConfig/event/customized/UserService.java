package com.seamew.annotationConfig.event.customized;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService
{
    @Value("#{'hacker, admin'.split(',')}")
    private List<String> blockedUsername;
    private ApplicationEventPublisher publisher;

    public List<String> getBlockedUsername()
    {
        return blockedUsername;
    }

    public void setBlockedUsername(List<String> blockedUsername)
    {
        this.blockedUsername = blockedUsername;
    }

    public ApplicationEventPublisher getPublisher()
    {
        return publisher;
    }

    @Autowired
    public void setPublisher(ApplicationEventPublisher publisher)
    {
        this.publisher = publisher;
    }

    public boolean login(User user)
    {
        if (blockedUsername.contains(user.getUsername()))
        {
            log.info("The login request from user [{}] is blocked", user);
            UserBlockedEvent event = new UserBlockedEvent(user.getUsername());
            publisher.publishEvent(event);
            return false;
        }
        else
        {
            log.info("User [{}] login", user);
            return true;
        }
    }
}
