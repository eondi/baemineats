package com.sparta.baemineats.dto.requestDto;

import com.sparta.baemineats.entity.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequestDto {

    @Pattern(regexp ="^[a-z0-9]{4,10}", message = "username은 숫자 및 알파벳 소문자 4~10자로 입력해주세요.")
    private String userName;

    @Pattern(regexp ="^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]{8,15}", message = "비밀번호는 숫자 및 알파벳 대소문자 그리고 특수문자를 포함한 8~15자로 입력해주세요.")
    private String password;

    @Email(message = "옳바른 email 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "주소를 필수로 입력 해주세요.")
    private String address;

    private UserRoleEnum role;
}
