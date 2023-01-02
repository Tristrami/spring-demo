package com.seamew.validator.standard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

@Service
public class PersonService
{
    @Autowired
    private Validator validator;

    public Validator getValidator()
    {
        return validator;
    }

    public void setValidator(Validator validator)
    {
        this.validator = validator;
    }
}
