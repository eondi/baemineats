package com.sparta.baemineats.dto.responseDto;

import com.sparta.baemineats.entity.Store;

import java.time.LocalDateTime;

public class StroeResponse {
    private Long storeId;
    private String storeName;
    private String storeDescription;


    public StroeResponse(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.storeDescription = store.getStoreDescription();
    }

    public StroeResponse(String storeName) {
        this.storeName = storeName;
    }
}
