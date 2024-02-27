package com.sparta.baemineats.dto.responseDto;

import lombok.Getter;

@Getter
public class LikeUserResponse {
    private String username;

    public LikeUserResponse(String username) {
        this.username =username;
    }
}
