package com.iko.restapi.dto.user.validation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NotBlank
@Min(value = 2, message = "유저 이름은 2자 이상입니다")
@Max(value = 20, message = "유저 이름은 20자 이하입니다")
@Pattern(regexp = "[a-zA-Z0-9가-힣]{2,20}", message = "유저 이름은 영어, 숫자, 한글로 이루어져야 합니다")
public @interface Username {
}
