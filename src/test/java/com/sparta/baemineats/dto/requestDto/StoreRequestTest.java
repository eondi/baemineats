package com.sparta.baemineats.dto.requestDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoreRequestTest {

    @Test
    @DisplayName("StoreRequestDto test")
    public void RequestDtoTest(){
        //given;
        String storeName = "testStoreName";
        String storeDescription = "testStoreDescription";

        //when
        StoreRequest storeRequestDto = new StoreRequest(storeName,storeDescription);

        //then
        assertEquals("testStoreName",storeRequestDto.getStoreName());
        assertEquals("testStoreDescription",storeRequestDto.getStoreDescription());

    }

}