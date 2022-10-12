package com.iko.restapi.dto.user.validation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NotBlank
@Pattern(regexp = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$", message = "적절하지 않은 전화번호 형식입니다")
public @interface Phone {
}
