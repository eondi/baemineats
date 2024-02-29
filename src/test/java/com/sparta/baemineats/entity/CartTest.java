package com.sparta.baemineats.entity;

import com.sparta.baemineats.dto.requestDto.CartRequest;
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
public class CartTest {
    @Mock
    StoreRepository storeRepository;
    @Test
    @DisplayName("장바구니 생성")
    public void test() throws IOException {
        //given
        String menuName = "탕수육";
        int menuPrice = 20000;
        String menuDescription = "돼지고기 : 국내산";
        String imageUrl = "http://localhost:8080/uploads/b.png";

        MockMultipartFile image = new MockMultipartFile(
                "test2",
                "b.png",
                "image/png",
                new FileInputStream(new File("C:/Users/Owner/Pictures/Screenshots/b.png")));

        MenuRequest requestDto = new MenuRequest(menuName, menuPrice, menuDescription, image);

        String storeName = "홍콩반점";
        String storeDescription = "맛있는 가게";
        StoreRequest request = new StoreRequest(storeName,storeDescription);
        Store store = storeRepository.save(new Store(request, "사장"));

        Menu menu = new Menu(requestDto, store, imageUrl);
        User user = new User("이름","패스워드","주소","이메일");
        CartRequest cartRequest = new CartRequest(1L,1L,1L,1L,15000);
        //when
        Cart cart = new Cart(cartRequest, user, store, menu);

        //then
        assertNull(cart.getCartId());
        assertEquals(1L,cart.getQuantity());
        assertEquals(15000,cart.getTotalPrice());
    }
}
