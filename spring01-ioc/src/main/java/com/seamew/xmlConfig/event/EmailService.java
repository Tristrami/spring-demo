package com.seamew.xmlConfig.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.List;

@Slf4j
public class EmailService implements ApplicationEventPublisherAware
{
    private ApplicationEventPublisher publisher;
    private List<String> blockList;

    public ApplicationEventPublisher getPublisher()
    {
        return publisher;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
    {
        this.publisher = applicationEventPublisher;
    }

    public List<String> getBlockList()
    {
        return blockList;
    }

    public void setBlockList(List<String> blockList)
    {
        this.blockList = blockList;
    }

    public void sendEmail(String address, String content)
    {
        if (blockList.contains(address))
        {
            log.info("Address [{}] is blocked", address);
            publisher.publishEvent(new EmailBlockedEvent(this, address, content));
        }
        else
        {
            log.info("Sending email to [{}]", address);
        }
    }
}
