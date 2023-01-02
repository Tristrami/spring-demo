package com.seamew.service.impl;

import com.seamew.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/* 使用编程式的方式实现事务 */

@Service("userServiceImpl")
@Slf4j
public class UserServiceImpl implements IUserService
{
    private JdbcTemplate jdbcTemplate;
    private PlatformTransactionManager txManager;
    private TransactionTemplate txTemplate;

    @Autowired
    public UserServiceImpl(JdbcTemplate jdbcTemplate, PlatformTransactionManager txManager, TransactionTemplate txTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.txManager = txManager;
        this.txTemplate = txTemplate;
    }

    @Override
    public void modifyPassword(String username, String newPassword)
    {
        TransactionDefinition txDefinition = new DefaultTransactionDefinition();
        TransactionStatus txStatus = txManager.getTransaction(txDefinition);
        try
        {
            String sql = "update user set password = ? where username = ?;";
            log.info("Modify password of user [{}], new password: [{}]", username, newPassword);
            jdbcTemplate.update(sql, newPassword, username);
            txManager.commit(txStatus);
        }
        catch (Exception e)
        {
            txManager.rollback(txStatus);
            e.printStackTrace();
        }
    }

    @Override
    public void modifyUsername(Long userId, String newUsername)
    {
        // execute() 方法将需要事物的代码用 try catch 语块包裹住
        txTemplate.execute((status -> {
            String sql = "update user set username = ? where id = ?;";
            log.info("Modify username to [{}]", newUsername);
            return jdbcTemplate.update(sql, newUsername, userId);
        }));
    }
}
