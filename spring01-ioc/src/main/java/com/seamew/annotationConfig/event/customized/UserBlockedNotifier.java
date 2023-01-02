package com.seamew.annotationConfig.event.customized;

import com.seamew.annotationConfig.event.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

@Slf4j
@Listener("userBlockNotifier")
public class UserBlockedNotifier
{
    @EventListener
    @Order(2)
    public UserWarningEvent onUserLoginBlocked(UserBlockedEvent event)
    {
        log.info("Notify user [{}]", event.getUsername());

        // UserBlockedEvent 事件触发 UserWarningEvent 事件
        return new UserWarningEvent("User warning: user [" + event.getUsername() + "] is on the block list!");
    }
}
