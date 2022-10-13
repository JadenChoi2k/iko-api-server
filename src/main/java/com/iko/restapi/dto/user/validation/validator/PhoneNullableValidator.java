package com.iko.restapi.dto.user.validation.validator;

import com.iko.restapi.dto.user.validation.annotation.PhoneNullable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNullableValidator implements ConstraintValidator<PhoneNullable, String> {
    private static final String REGEX = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.matches(REGEX);
    }

    @Override
    public void initialize(PhoneNullable constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
