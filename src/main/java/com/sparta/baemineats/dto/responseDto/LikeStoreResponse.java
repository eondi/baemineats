package com.sparta.baemineats.dto.responseDto;

import lombok.Getter;

@Getter
public class LikeStoreResponse {
    private String storeName;

    public LikeStoreResponse(String storeName) {
        this.storeName =storeName;
    }
}
