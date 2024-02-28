package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.StoreRequest;
import com.sparta.baemineats.dto.responseDto.StoreResponse;
import com.sparta.baemineats.entity.Store;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.entity.UserRoleEnum;
import com.sparta.baemineats.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest{

    String ANOTHER_PREFIX = "another-";
    String ADMIN_PREFIX = "admin-";
    String SELLER_PREFIX = "seller-";
    Long TEST_USER_ID = 1L;
    Long TEST_ANOTHER_USER_ID = 2L;
    Long TEST_ADMIN_USER_ID = 3L;
    Long TEST_SELLER_USER_ID = 4L;
    String TEST_USER_NAME = "username";
    String TEST_USER_PASSWORD = "password";
    String TEST_USER_ADDRESS = "address";
    String TEST_USER_EMAIL = "email@test.com";
//
    String STORE_NAME = "햄버거";
    String STORE_DESCRIPTION = "맛도리 햄버거";



    @InjectMocks
    StoreService storeService;

    @Mock
    StoreRepository storeRepository;

    User TEST_SELLER1 = User.builder()
            .id(TEST_SELLER_USER_ID)
            .username(SELLER_PREFIX + TEST_USER_NAME)
            .password(SELLER_PREFIX + TEST_USER_PASSWORD)
            .address(TEST_USER_ADDRESS)
            .email(SELLER_PREFIX + TEST_USER_EMAIL)
            .role(UserRoleEnum.SELLER)
            .build();

    User TEST_ADMIN = User.builder()
            .id(TEST_ADMIN_USER_ID)
            .username(ADMIN_PREFIX + TEST_USER_NAME)
            .password(ADMIN_PREFIX + TEST_USER_PASSWORD)
            .address(TEST_USER_ADDRESS)
            .email(ADMIN_PREFIX + TEST_USER_EMAIL)
            .role(UserRoleEnum.ADMIN)
            .build();

    Store TEST_STORE1 = Store.builder()
            .id(1L)
            .storeName("치킨")
            .storeDescription("맛도리 치킨")
            .username(SELLER_PREFIX + TEST_USER_NAME)
            .build();

    Store TEST_STORE2 = Store.builder()
            .id(2L)
            .storeName("피자")
            .storeDescription("맛도리 피자")
            .username(SELLER_PREFIX + TEST_USER_NAME)
            .build();

    Store TEST_STORE3 = Store.builder()
            .id(3L)
            .storeName("햄버거")
            .storeDescription("맛도리 햄버거")
            .username("test_seller2")
            .build();

    StoreRequest TEST_STORE_REQUEST_DTO = new StoreRequest(STORE_NAME,STORE_DESCRIPTION);


    List<Store> storeList = new ArrayList<>();




    @DisplayName("음식점 전체 조회")
    @Test
    void getStoreAllTest() {
        // given
        storeList.add(TEST_STORE1);
        storeList.add(TEST_STORE2);
        storeList.add(TEST_STORE3);

        doReturn(storeList).when(storeRepository).findAll();


         // when
         List<StoreResponse> res = storeService.getStoreAll();

        // then
        assertEquals(3,res.size());
        assertEquals("치킨", res.get(0).getStoreName());
        assertEquals("피자", res.get(1).getStoreName());
        assertEquals("햄버거", res.get(2).getStoreName());
    }

    @DisplayName("특정 음식점 조회")
    @Test
    void getStoreTest() {
        // given
        given(storeRepository.findById(2L)).willReturn(Optional.of(TEST_STORE2));

        // when
        StoreResponse res = storeService.getStore(2L, TEST_SELLER1);

        // then
        assertEquals(2L,res.getStoreId());
        assertEquals("피자", res.getStoreName());
        assertEquals("맛도리 피자", res.getStoreDescription());
    }

    @DisplayName("특정 음식점 조회 에러 테스트")
    @Test
    void getStoreErrorTest() {
        // given
        given(storeRepository.findById(5L)).willReturn(Optional.of(TEST_STORE2));

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            storeService.getStore(5L,TEST_SELLER1);
        });
        // then
        assertEquals(
                "해당 id의 음식점이 없습니다.",
                exception.getMessage()
        );
    }

    @DisplayName("음식점 수정")
    @Test
    void updateStoreTest() {
        // given
        given(storeRepository.findById(2L)).willReturn(Optional.of(TEST_STORE2));

        // when
        StoreResponse res = storeService.updateStore(2L, TEST_STORE_REQUEST_DTO, TEST_SELLER1);

        // then
        assertEquals(2L,res.getStoreId());
        assertEquals("햄버거", res.getStoreName());
        assertEquals("맛도리 햄버거", res.getStoreDescription());
    }


    @DisplayName("음식점 수정 에러 - 자기자신의 음식점이 아닌경우")
    @Test
    void updateStoreErrorTest() {
        // given
        given(storeRepository.findById(2L)).willReturn(Optional.of(TEST_STORE2));

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            storeService.updateStore(2L, TEST_STORE_REQUEST_DTO, TEST_ADMIN);
        });
        // then
        assertEquals(
                "다른 판매자의 매장 수정은 불가능합니다.",
                exception.getMessage()
        );
    }

    @DisplayName("음식점 삭제")
    @Test
    void deleteStoreTest() {
        // given
        given(storeRepository.findById(2L)).willReturn(Optional.of(TEST_STORE2));

        // when
        String res = storeService.deleteStore(2L, TEST_SELLER1);

        // then
        assertEquals("피자", res);
    }


    @DisplayName("음식점 삭제 에러 - 자기자신의 음식점이 아닌경우")
    @Test
    void deleteStoreErrorTest() {
        // given
        given(storeRepository.findById(2L)).willReturn(Optional.of(TEST_STORE2));

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            storeService.deleteStore(2L, TEST_ADMIN);
        });
        // then
        assertEquals(
                "다른 판매자의 매장 삭제는 불가능합니다.",
                exception.getMessage()
        );
    }



}