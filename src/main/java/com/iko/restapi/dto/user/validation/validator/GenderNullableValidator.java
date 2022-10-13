package com.iko.restapi.dto.user.validation.validator;

import com.iko.restapi.dto.user.validation.annotation.GenderNullable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GenderNullableValidator implements ConstraintValidator<GenderNullable, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.matches("[GMX]");
    }

    @Override
    public void initialize(GenderNullable constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
