package com.sparta.baemineats.dto.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserModifyAllRequestDto {

    @NotNull(message = "한 줄 소개를 필수로 입력해주세요.")
    private String description;

    @Email(message = "옳바른 email 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "주소를 필수로 입력해주세요.")
    private String address;

}
