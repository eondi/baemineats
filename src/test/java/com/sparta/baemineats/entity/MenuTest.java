//package com.sparta.baemineats.entity;
//
//import com.sparta.baemineats.dto.requestDto.MenuRequest;
//import com.sparta.baemineats.dto.requestDto.StoreRequest;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//class MenuTest {
//    @Test
//    @DisplayName("메뉴 생성")
//    public void test()throws IOException {
//        //given
//        String menuName = "탕수육";
//        int menuPrice = 25000;
//        String menuDescription = "돼지고기 : 국내산";
//
//        MockMultipartFile image = new MockMultipartFile(
//                "test",
//                "a.png",
//                "image/png",
//                new FileInputStream(new File("C:/Users/Owner/Pictures/Screenshots/a.png")));
//
//        MenuRequest request = new MenuRequest(menuName,menuPrice,menuDescription,image);
//
//        String storeName = "홍콩반점";
//        String storeDescription = "맛있는 가게";
//        StoreRequest request = new StoreRequest(storeName,storeDescription);
//        storeRepository.save(new Store(request, "사장"));
//
//        //when
//        Menu menu = new Menu(request,);
//
//        //then
//        assertNull(storeTest.getStoreId());
//        assertEquals("testStoreName",storeTest.getStoreName());
//        assertEquals("testStoreDescription",storeTest.getStoreDescription());
//        assertEquals("testSellerName",storeTest.getSellerName());
//
//    }
//}
