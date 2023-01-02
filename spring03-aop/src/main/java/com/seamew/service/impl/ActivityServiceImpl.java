package com.seamew.service.impl;

import com.seamew.service.IActivityService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActivityServiceImpl implements IActivityService
{
    @Override
    public void moneyOff()
    {
        log.debug("money off");
    }
}
