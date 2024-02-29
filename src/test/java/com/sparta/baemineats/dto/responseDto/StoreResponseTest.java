package com.sparta.baemineats.dto.responseDto;

import com.sparta.baemineats.dto.requestDto.StoreRequest;
import com.sparta.baemineats.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoreResponseTest {
    @Test
    @DisplayName("StoreRequestDto test")
    public void ResponseDtoTest(){
        //given
        String storeName = "testStoreName";
        String storeDescription = "testStoreDescription";
        StoreRequest storeRequestDto = new StoreRequest(storeName,storeDescription);

        Store TestStore = new Store(storeRequestDto, "testSellerName");
        //when
        StoreResponse responseTest = new StoreResponse(TestStore);

        //then
        assertEquals("testStoreName",responseTest.getStoreName());
        assertEquals("testStoreDescription",storeRequestDto.getStoreDescription());



    }

}