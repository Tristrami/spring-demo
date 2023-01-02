package com.seamew.dataBinder;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator
{
    @Override
    public boolean supports(Class<?> clazz)
    {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        User user = (User) target;
        String username = user.getUsername();
        if (username.length() > 10)
        {
            errors.rejectValue("username", "200", "用户名不能超过十位字符");
        }
    }
}
