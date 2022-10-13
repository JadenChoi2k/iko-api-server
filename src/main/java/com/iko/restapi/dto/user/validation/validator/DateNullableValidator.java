package com.iko.restapi.dto.user.validation.validator;

import com.iko.restapi.dto.user.validation.annotation.DateNullable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateNullableValidator implements ConstraintValidator<DateNullable, String> {
    private static final String REGEX = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.matches(REGEX);
    }

    @Override
    public void initialize(DateNullable constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
