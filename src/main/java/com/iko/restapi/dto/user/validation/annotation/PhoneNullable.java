package com.iko.restapi.dto.user.validation.annotation;

import com.iko.restapi.dto.user.validation.validator.PhoneNullableValidator;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;

// null 가능 필드
//@Pattern(regexp = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$", message = "적절하지 않은 전화번호 형식입니다")
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNullableValidator.class)
public @interface PhoneNullable {
    String message() default "적절하지 않은 전화번호 형식입니다";
    Class[] groups() default {};
    Class[] payload() default {};
}

