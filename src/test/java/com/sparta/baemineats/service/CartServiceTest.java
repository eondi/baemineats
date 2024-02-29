package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.CartRequest;
import com.sparta.baemineats.dto.requestDto.MenuRequest;
import com.sparta.baemineats.dto.requestDto.StoreRequest;
import com.sparta.baemineats.dto.responseDto.CartResponse;
import com.sparta.baemineats.entity.Menu;
import com.sparta.baemineats.entity.Store;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.repository.CartRepository;
import com.sparta.baemineats.repository.MenuRepository;
import com.sparta.baemineats.repository.StoreRepository;
import com.sparta.baemineats.repository.UserRepository;
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
class CartServiceTest {
    @Autowired
    CartService cartService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    UserRepository userRepository;

    User user = new User("asdf1234", "asdf1234A!", "경기도", "asd@email.com");
    Long cartMenuId;

    @Test
    @Order(1)
    @DisplayName("장바구니 추가하기")
    void addToCart() throws IOException {
        // Given

        String storeName = "홍콩반점";
        String storeDescription = "맛있는 가게";
        StoreRequest request = new StoreRequest(storeName,storeDescription);
        Store store = storeRepository.save(new Store(request, "사장")); // 가게 저장

        String menuName = "탕수육";
        int menuPrice = 25000;
        String menuDescription = "돼지고기 : 국내산";
        String imageUrl = "http://localhost:8080/uploads/a.png";
        MockMultipartFile image = new MockMultipartFile(
                "test",
                "a.png",
                "image/png",
                new FileInputStream(new File("C:/Users/Owner/Pictures/Screenshots/a.png")));

        MenuRequest requestDto = new MenuRequest(menuName, menuPrice, menuDescription, image);
        Menu menu = menuRepository.save(new Menu(requestDto, store, imageUrl)); // 메뉴 저장

        User cartUser = userRepository.save(user); // 유저 저장

        CartRequest cartRequest = new CartRequest(store.getStoreId(), cartUser.getUserId(), menu.getMenuId(), 1L, 25000);

        // When
        CartResponse cartResponse = cartService.addToCart(cartRequest, user);

        // Then
        assertEquals(1L, cartResponse.getQuantity());
        assertEquals(25000, cartResponse.getTotalPrice());
        this.cartMenuId = cartResponse.getMenuId();
    }

    @Test
    @Order(2)
    @DisplayName("장바구니 조회하기")
    void getCarts() {
        // Given
        String storeName = "홍콩반점";
        String menuName = "탕수육";
        // When
        List<CartResponse> cartList = cartService.getCarts();
        // Then
        CartResponse createdCart = cartList.stream()
                .findFirst()
                .orElse(null);

        assertNotNull(cartList);
        assertEquals(storeName, createdCart.getStoreName());
        assertEquals(menuName, createdCart.getMenuName());
    }

    @Test
    @Order(3)
    @DisplayName("장바구니 삭제하기")
    void deleteCart() {
        // Given
        Long menuId = cartMenuId;
        // When
        cartService.deleteCart(menuId,user);
        // Then
        List<CartResponse> cartResponse = cartService.getCarts();

        assertEquals(0,cartResponse.size());
    }
}