package com.seamew.annotationConfig.event.customized;

import com.seamew.annotationConfig.event.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

@Slf4j
@Listener
public class UserBlockedListener
{
    @EventListener
    @Order(1)
    public void onUserBlocked(UserBlockedEvent event)
    {
        log.info("User [{}] is blocked", event.getUsername());
    }
}
