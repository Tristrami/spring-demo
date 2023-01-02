package com.seamew.annotationConfig.event.standard;

import com.seamew.annotationConfig.event.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@Listener
public class ContextRefreshedEventListener
{
    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event)
    {
        log.info("Context refreshed");
    }
}