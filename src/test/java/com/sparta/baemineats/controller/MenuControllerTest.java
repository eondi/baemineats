package com.sparta.baemineats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.baemineats.config.WebSecurityConfig;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.entity.UserRoleEnum;
import com.sparta.baemineats.repository.StoreRepository;
import com.sparta.baemineats.security.UserDetailsImpl;
import com.sparta.baemineats.service.MenuService;
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
        controllers = {UserController.class, MenuController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)

@DisabledInAotMode
class MenuControllerTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    MenuService menuService;

    @MockBean
    StoreRepository storeRepository;

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

    @Test
    @DisplayName("새로운 메뉴 등록")
    void test1() throws Exception {
        // given
        this.mockUserSetup();

        MockMultipartFile image = new MockMultipartFile(
                "test",
                "a.png",
                "image/png",
                new FileInputStream(new File("C:/Users/Owner/Pictures/Screenshots/a.png")));


        // when - then
        mvc.perform(
                multipart("/api/menus/stores/{storeId}",1)
                        .file(image)
                        .param("menuName", "탕수육")
                        .param("menuPrice", "25000")
                        .param("menuDescription", "돼지고기 : 국내산")
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("전체 메뉴 조회")
    void test2() throws Exception {
        this.mockUserSetup();

        // when - then
        mvc.perform(get("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("단일 메뉴 조회")
    void test3() throws Exception {
        this.mockUserSetup();

        // when - then
        mvc.perform(get("/api/menus/{menuId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("메뉴 삭제")
    void test5() throws Exception {
        this.mockUserSetup();

        // when - then
        mvc.perform(delete("/api/menus/{menuId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
