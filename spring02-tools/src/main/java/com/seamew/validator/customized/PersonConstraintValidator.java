package com.seamew.validator.customized;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PersonConstraintValidator implements ConstraintValidator<PersonConstraint, Person>
{
    private int minAge;
    private int maxAge;

    @Override
    public void initialize(PersonConstraint constraintAnnotation)
    {
        this.minAge = constraintAnnotation.minAge();
        this.maxAge = constraintAnnotation.maxAge();
    }

    @Override
    public boolean isValid(Person value, ConstraintValidatorContext context)
    {
        int age = value.getAge();
        return age >= minAge && age <= maxAge;
    }
}
