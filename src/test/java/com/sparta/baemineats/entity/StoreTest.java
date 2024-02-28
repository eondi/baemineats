package com.sparta.baemineats.entity;

import com.sparta.baemineats.dto.requestDto.StoreRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class StoreTest {
    @Test
    @DisplayName("Store 생성 test")
    public void createStore(){
        //given
        String storeName = "testStoreName";
        String storeDescription = "testStoreDescription";
        String sellerName = "testSellerName";

        StoreRequest storeRequest = new StoreRequest(storeName,storeDescription);


        //when
        Store storeTest = new Store(storeRequest, sellerName);

        //then
        assertNull(storeTest.getStoreId());
        assertEquals("testStoreName",storeTest.getStoreName());
        assertEquals("testStoreDescription",storeTest.getStoreDescription());
        assertEquals("testSellerName",storeTest.getSellerName());

    }


}