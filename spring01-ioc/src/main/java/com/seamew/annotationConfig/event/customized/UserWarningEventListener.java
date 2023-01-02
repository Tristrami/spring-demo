package com.seamew.annotationConfig.event.customized;

import com.seamew.annotationConfig.event.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

@Slf4j
@Listener("userWarningEventListener")
public class UserWarningEventListener
{
    @EventListener
    public void onUserWarning(UserWarningEvent event)
    {
        log.info("{}", event.getMessage());
    }
}
