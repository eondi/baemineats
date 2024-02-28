package com.sparta.baemineats.repository;

import com.sparta.baemineats.dto.requestDto.StoreRequest;
import com.sparta.baemineats.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StoreRepositoryTest {
    @Autowired
    private StoreRepository storeRepository;


    String storeName = "testStoreName";
    String storeDescription = "testStoreDescription";
    String sellerName = "testSellerName";

    StoreRequest storeRequest = new StoreRequest(storeName,storeDescription);
    Store storeTest = new Store(storeRequest, sellerName);


    @Test
    @DisplayName("음식점 추가 및 검색 테스트")
    void addStoreTest() {

        // given

        // when
        Store savedStore = storeRepository.save(storeTest);

        // then
        assertEquals(savedStore.getStoreName(),storeTest.getStoreName());
        assertEquals(savedStore.getStoreDescription(),storeTest.getStoreDescription());
        assertEquals(savedStore.getSellerName(),storeTest.getSellerName());

    }

    @Test
    @DisplayName("음식점 추가 및 검색 테스트")
    void findBySellerNameErrorTest() {

        // given
        Store savedStore = storeRepository.save(storeTest);

        //when
        Store findStore = storeRepository.findBySellerName("sellerName");

        //then
        assertNull(findStore);

    }



}