package com.sparta.baemineats.entity;

import com.sparta.baemineats.dto.requestDto.MenuRequest;
import com.sparta.baemineats.dto.requestDto.StoreRequest;
import com.sparta.baemineats.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class MenuTest {
    @Mock
    StoreRepository storeRepository;

    @Test
    @DisplayName("메뉴 생성")
    public void test()throws IOException {
        //given
        String menuName = "탕수육";
        int menuPrice = 25000;
        String menuDescription = "돼지고기 : 국내산";
        String imageUrl = "http://localhost:8080/uploads/b.png";

        MockMultipartFile image = new MockMultipartFile(
                "test",
                "a.png",
                "image/png",
                new FileInputStream(new File("C:/Users/Owner/Pictures/Screenshots/a.png")));

        MenuRequest request = new MenuRequest(menuName,menuPrice,menuDescription,image);

        String storeName = "홍콩반점";
        String storeDescription = "맛있는 가게";
        StoreRequest storeRequest = new StoreRequest(storeName,storeDescription);
        Store store = storeRepository.save(new Store(storeRequest, "사장"));

        //when
        Menu menu = new Menu(request, store, imageUrl);

        //then
        assertNull(menu.getMenuId());
        assertEquals("탕수육",menu.getMenuName());
        assertEquals("돼지고기 : 국내산",menu.getMenuDescription());

    }
}
