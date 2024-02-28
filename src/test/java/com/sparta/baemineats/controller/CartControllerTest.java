package com.sparta.baemineats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.baemineats.config.WebSecurityConfig;
import com.sparta.baemineats.dto.requestDto.CartRequest;
import com.sparta.baemineats.dto.requestDto.MenuRequest;
import com.sparta.baemineats.dto.requestDto.StoreRequest;
import com.sparta.baemineats.entity.Menu;
import com.sparta.baemineats.entity.Store;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.entity.UserRoleEnum;
import com.sparta.baemineats.repository.MenuRepository;
import com.sparta.baemineats.repository.StoreRepository;
import com.sparta.baemineats.security.UserDetailsImpl;
import com.sparta.baemineats.service.CartService;
import com.sparta.baemineats.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {UserController.class, CartController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)

@DisabledInAotMode
class CartControllerTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    CartService cartService;

    @MockBean
    StoreRepository storeRepository;

    @MockBean
    MenuRepository menuRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {

        String username = "asdf1234";
        String password = "asdf1234A!";
        String address = "경기도";
        String email = "asd@email.com";
        UserRoleEnum role = UserRoleEnum.SELLER;
        User testUser = User.builder()
                .id(1L)
                .username(username)
                .password(password)
                .address(address)
                .email(email)
                .role(role)
                .build();
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    Long storeId;
    Long userId = 1L;
    Long menuId;

    @Test
    @DisplayName("장바구니 추가")
    void test() throws Exception {
        // given
        this.mockUserSetup();

        String storeName = "홍콩반점";
        String storeDescription = "맛있는 가게";
        StoreRequest requestDto = new StoreRequest(storeName,storeDescription);
        Store store = storeRepository.save(new Store(requestDto, "사장"));
        this.storeId = store.getStoreId();

        String menuName = "탕수육";
        int menuPrice = 25000;
        String menuDescription = "돼지고기 : 국내산";
        String imageUrl = "http://localhost:8080/uploads/b.png";

        MockMultipartFile image = new MockMultipartFile(
                "test",
                "a.png",
                "image/png",
                new FileInputStream(new File("C:/Users/Owner/Pictures/Screenshots/a.png")));

        MenuRequest menuRequest = new MenuRequest(menuName, menuPrice, menuDescription, image);
        Menu menu = new Menu(menuRequest, store, imageUrl);
        Menu savedMenu = menuRepository.save(menu);
        this.menuId = savedMenu.getMenuId();

        Long quantity = 1L;
        int totalPrice = 25000;
        CartRequest request = new CartRequest(storeId,userId,menuId,quantity,totalPrice);

        String postInfo = objectMapper.writeValueAsString(request);

        // when - then
        mvc.perform(post("/api/carts/add")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("장바구니 조회")
    void test2() throws Exception {
        this.mockUserSetup();

        mvc.perform(get("/api/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("장바구니 삭제")
    void test3() throws Exception {
        this.mockUserSetup();

        mvc.perform(delete("/api/carts/{menuId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}