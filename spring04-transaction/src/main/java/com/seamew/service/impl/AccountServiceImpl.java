package com.seamew.service.impl;

import com.seamew.service.IAccountService;
import com.seamew.service.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AccountServiceImpl implements IAccountService
{
    private JdbcTemplate template;
    private ILogService logService;

    @Autowired
    public AccountServiceImpl(JdbcTemplate template, ILogService logService)
    {
        this.template = template;
        this.logService = logService;
    }

    @Override
    @Transactional
    public void transfer(String from, String to, Double amount)
    {
        String sql1 = "update account set balance = balance - ? where username = ?;";
        String sql2 = "update account set balance = balance + ? where username = ?;";

        logService.addLog("transfer [" + amount + "] from account [" + from + "] to account [" + to + "]");
        log.info("Deduct [{}] from account [{}]", amount, from);
        template.update(sql1, amount, from);

        log.info("Recharge [{}] to account [{}]", amount, to);
        template.update(sql2, amount, to);
    }

    @Override
    @Transactional
    public void transferThrowingException(String from, String to, Double amount)
    {
        String sql1 = "update account set balance = balance - ? where username = ?;";
        String sql2 = "update account set balance = balance + ? where username = ?;";

        log.info("Deduct [{}] from account [{}]", amount, from);
        template.update(sql1, amount, from);
        logService.addLog("transfer [" + amount + "] from account [" + from + "] to account [" + to + "]");

        int i = 1 / 0;

        log.info("Recharge [{}] to account [{}]", amount, to);
        template.update(sql2, amount, to);
    }
}
