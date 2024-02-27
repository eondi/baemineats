package com.sparta.baemineats.dto.responseDto;

import com.sparta.baemineats.entity.Store;

public class StoreResponse {
    private Long storeId;
    private String storeName;
    private String storeDescription;


    public StoreResponse(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.storeDescription = store.getStoreDescription();
    }

    public StoreResponse(String storeName) {
        this.storeName = storeName;
    }
}
