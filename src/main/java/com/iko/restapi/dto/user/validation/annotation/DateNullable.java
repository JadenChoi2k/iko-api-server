package com.iko.restapi.dto.user.validation.annotation;

import com.iko.restapi.dto.user.validation.validator.DateNullableValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateNullableValidator.class)
public @interface DateNullable {
    String message() default "적절하지 않은 날짜형식입니다";
    Class[] groups() default {};
    Class[] payload() default {};
}
