package com.iko.restapi.dto.user.validation;

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
@Pattern(regexp="^((19|20)\\\\d\\\\d)?([- /.])?(0[1-9]|1[012])([- /.])?(0[1-9]|[12][0-9]|3[01])$")
public @interface DateFormat {
}
