package com.seamew.annotationConfig.event.standard;

import com.seamew.annotationConfig.event.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@Listener
public class ContextClosedEventListener
{
    @EventListener
    public void onContextClosed(ContextClosedEvent event)
    {
        log.info("Context closed");
    }
}
