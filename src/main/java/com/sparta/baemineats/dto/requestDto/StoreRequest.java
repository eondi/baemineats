package com.sparta.baemineats.dto.requestDto;

import lombok.Getter;

@Getter
public class StoreRequest {
    private String storeName;

    private String storeDescription;

    public StoreRequest(String storeName, String storeDescription) {
        this.storeName = storeName;
        this.storeDescription = storeDescription;
    }
}
