package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.MenuRequest;
import com.sparta.baemineats.dto.responseDto.MenuResponse;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.entity.UserRoleEnum;
import com.sparta.baemineats.repository.MenuRepository;
import com.sparta.baemineats.repository.StoreRepository;
import com.sparta.baemineats.security.UserDetailsImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MenuServiceIntegrationTest {

    @Autowired
    MenuService menuService;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    UploadService uploadService;
    @Autowired
    StoreRepository storeRepository;

    Long createdId;

    @Test
    @Order(1)
    @DisplayName("메뉴 등록하기")
    void test1() throws IOException {
        // given
        Long storeId = 1L;
        String menuName = "탕수육";
        int menuPrice = 25000;
        String menuDescription = "돼지고기 : 국내산";

        MockMultipartFile image = new MockMultipartFile(
                "test",
                "a.png",
                "image/png",
                new FileInputStream(new File("C:/Users/Owner/Pictures/Screenshots/a.png")));

        MenuRequest requestDto = new MenuRequest(menuName, menuPrice, menuDescription, image);

        // when - then
        menuService.createMenu(storeId, requestDto);

        List<MenuResponse> menus = menuService.getMenus();
        MenuResponse createdMenu = menus.get(menus.size() - 1); // 가장 최근에 등록된 메뉴 가져오기
        this.createdId = createdMenu.getMenuId();

        assertNotNull(createdId);
    }

    @Test
    @Order(2)
    @DisplayName("메뉴 가격, 이미지 변경하기")
    void test2() throws IOException {
        // given
        Long menuId = createdId;
        String menuName = "탕수육";
        int menuPrice = 20000;
        String menuDescription = "돼지고기 : 국내산";

        MockMultipartFile image = new MockMultipartFile(
                "test2",
                "b.png",
                "image/png",
                new FileInputStream(new File("C:/Users/Owner/Pictures/Screenshots/b.png")));

        MenuRequest requestDto = new MenuRequest(menuName, menuPrice, menuDescription, image);

        // when - then
        menuService.updateMenu(menuId, requestDto);
    }

    @Test
    @Order(3)
    @DisplayName("메뉴 가격, 이미지 변경되었는지 조회하기")
    void test3() {
        // given
        Long menuId = createdId;
        String imageUrl = "/uploads/b.png";
        int menuPrice = 20000;

        // when
        List<MenuResponse> menu = menuService.getMenuFindById(menuId);

        // then
        MenuResponse updatedMenu = menu.stream()
                .filter(menuResponse -> menuResponse.getMenuName().equals("탕수육"))
                .findFirst()
                .orElse(null);

        assertNotNull(menu);
        assertEquals(imageUrl, updatedMenu.getImageUrl());
        assertEquals(menuPrice, updatedMenu.getMenuPrice());
    }
}