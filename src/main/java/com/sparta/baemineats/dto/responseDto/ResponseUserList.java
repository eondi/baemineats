package com.sparta.baemineats.dto.responseDto;

import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.entity.UserRoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ResponseUserList {

    private Long userId;

    private String username;

    private String email;

    private String address;

    private UserRoleEnum role;

    public ResponseUserList(@NotNull User user){
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.role = user.getRole();
    }
}
