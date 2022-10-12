package com.iko.restapi.dto.user.validation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NotBlank
@Pattern(regexp = "[a-zA-Z0-9]{8,30}", message = "아이디는 영어, 숫자로 이루어진 8~30자의 문자열입니다")
public @interface LoginId {
}
