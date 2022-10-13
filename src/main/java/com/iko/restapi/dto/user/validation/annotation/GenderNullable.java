package com.iko.restapi.dto.user.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GenderNullable {
    String message() default "성별은 M, F, X 중 하나의 값입니다";
    Class[] groups() default {};
    Class[] payload() default {};
}
