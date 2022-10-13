package com.iko.restapi.dto.user.validation.annotation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@NotBlank
@Pattern(regexp = "[a-zA-Z0-9]{8,30}", message = "아이디는 영어, 숫자로 이루어진 8~30자의 문자열입니다")
public @interface LoginId {
}
