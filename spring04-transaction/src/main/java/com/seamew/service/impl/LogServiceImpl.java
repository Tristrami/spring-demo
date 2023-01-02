package com.seamew.service.impl;

import com.seamew.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogServiceImpl implements ILogService
{
    private JdbcTemplate template;

    @Autowired
    public LogServiceImpl(JdbcTemplate template)
    {
        this.template = template;
    }

    @Override
    @Transactional
    public void addLog(String msg)
    {
        String sql = "insert into log (msg) values (?);";
        template.update(sql, msg);
    }
}
