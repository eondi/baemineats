package com.sparta.baemineats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.baemineats.config.WebSecurityConfig;
import com.sparta.baemineats.dto.requestDto.StoreRequest;
import com.sparta.baemineats.dto.responseDto.StoreResponse;
import com.sparta.baemineats.entity.Store;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.entity.UserRoleEnum;
import com.sparta.baemineats.security.UserDetailsImpl;
import com.sparta.baemineats.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {StoreController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
@DisabledInAotMode
@EnableMethodSecurity(securedEnabled = true)
class StoreControllerTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    StoreService storeService;

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

    User TEST_SELLER = User.builder()
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

    User TEST_USER = User.builder()
            .id(TEST_USER_ID)
            .username(TEST_USER_NAME)
            .password(TEST_USER_PASSWORD)
            .address(TEST_USER_ADDRESS)
            .email(TEST_USER_EMAIL)
            .role(UserRoleEnum.USER)
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
    StoreResponse TEST_STORE_RESONSE_DTO1 = new StoreResponse(TEST_STORE1);
    StoreResponse TEST_STORE_RESONSE_DTO2 = new StoreResponse(TEST_STORE2);
    StoreResponse TEST_STORE_RESONSE_DTO3 = new StoreResponse(TEST_STORE3);


    List<StoreResponse> storeResponseList = new ArrayList<>();



    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockSellerSetup() {

        // Mock 테스트 판매자 유저 생성
        UserDetailsImpl testUserDetails = new UserDetailsImpl(TEST_SELLER);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    private void mockUserSetup() {

        // Mock 테스트 유저 생성
        UserDetailsImpl testUserDetails = new UserDetailsImpl(TEST_USER);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    private void mockAdminSetup() {

        // Mock 테스트 관리자 생성
        UserDetailsImpl testUserDetails = new UserDetailsImpl(TEST_ADMIN);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("음식점 생성 테스트")
    void writeTest() throws Exception {

        // given
        this.mockSellerSetup();
        StoreRequest requestDto = new StoreRequest(STORE_NAME,STORE_DESCRIPTION);
        String postInfo = objectMapper.writeValueAsString(requestDto);

        // when - then
        ResultActions resultActions  = mvc.perform(post("/api/stores")
                .content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );


        resultActions.andExpect(status().isOk());

        resultActions.andExpect(content().json("{'httpStatus':200,'message': '정상적으로 가게가 등록되었습니다', 'data':null}"));
    }

    @Test
    @DisplayName("음식점 생성 기능 권한 테스트 - 관리자")
    void writeAuthTest() throws Exception {

        // given
        this.mockAdminSetup();
        StoreRequest requestDto = new StoreRequest(STORE_NAME,STORE_DESCRIPTION);
        String postInfo = objectMapper.writeValueAsString(requestDto);

        // when - then
        ResultActions resultActions  = mvc.perform(post("/api/stores")
                .content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );


        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("음식점 생성 기능 권한 테스트 - 일반 유저 400에러")
    void writeAuthErrorTest() throws Exception {

        // given
        this.mockUserSetup();
        StoreRequest requestDto = new StoreRequest(STORE_NAME,STORE_DESCRIPTION);
        String postInfo = objectMapper.writeValueAsString(requestDto);

        // when - then
        ResultActions resultActions  = mvc.perform(post("/api/stores")
                .content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );


        resultActions.andExpect(status().is4xxClientError());

        resultActions.andExpect(content().json("{\"statusCode\":400,\"state\":\"BAD_REQUEST\",\"message\":\"Access Denied\"}"));
    }

    @Test
    @DisplayName("음식점 전체 조회 테스트")
    void selectALlTest() throws Exception {

        // given
        this.mockSellerSetup();
        storeResponseList.add(TEST_STORE_RESONSE_DTO1);
        storeResponseList.add(TEST_STORE_RESONSE_DTO2);
        storeResponseList.add(TEST_STORE_RESONSE_DTO3);

        given(storeService.getStoreAll()).willReturn(storeResponseList);

        // when - then
        ResultActions resultActions  = mvc.perform(get("/api/stores/all")
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );

        resultActions.andExpect(status().isOk()).andReturn();

        resultActions.andExpect(content().json("{\"httpStatus\":200,\"message\":\"전체 목록 조회에 성공했습니다.\",\"data\":[{\"storeId\":1,\"storeName\":\"치킨\",\"storeDescription\":\"맛도리 치킨\"},{\"storeId\":2,\"storeName\":\"피자\",\"storeDescription\":\"맛도리 피자\"},{\"storeId\":3,\"storeName\":\"햄버거\",\"storeDescription\":\"맛도리 햄버거\"}]}"));

    }

    @Test
    @DisplayName("특정 음식점 조회 테스트")
    void selectTest() throws Exception {

        // given
        this.mockSellerSetup();

        given(storeService.getStore(1L, TEST_SELLER)).willReturn(TEST_STORE_RESONSE_DTO1);

        // when - then
        ResultActions resultActions  = mvc.perform(get("/api/stores/{storeId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );

        resultActions.andExpect(status().isOk()).andReturn();

        resultActions.andExpect(content().json("{\"httpStatus\":200,\"message\":\"해당 음식점 상세 조회에 성공했습니다.\",\"data\":{\"storeId\":1,\"storeName\":\"치킨\",\"storeDescription\":\"맛도리 치킨\"}}"));

    }




    @Test
    @DisplayName("음식점 수정 테스트")
    void updateTest() throws Exception {

        // given
        this.mockSellerSetup();
        String postInfo = objectMapper.writeValueAsString(TEST_STORE_REQUEST_DTO);

        given(storeService.updateStore(1L,TEST_STORE_REQUEST_DTO,TEST_SELLER)).willReturn(TEST_STORE_RESONSE_DTO1);

        // when - then
        ResultActions resultActions  = mvc.perform(put("/api/stores/{storeId}",1)
                .content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );

        resultActions.andExpect(status().isOk()).andReturn();

        resultActions.andExpect(content().json("{\"httpStatus\":200,\"message\":\"해당 음식점 수정을 성공했습니다.\",\"data\":null}"));

    }

    @Test
    @DisplayName("음식점 수정 권한 테스트 - 일반 유저  불가 ")
    void updateAuthErrorTest() throws Exception {

        // given
        this.mockUserSetup();
        String postInfo = objectMapper.writeValueAsString(TEST_STORE_REQUEST_DTO);

        given(storeService.updateStore(1L,TEST_STORE_REQUEST_DTO,TEST_SELLER)).willReturn(TEST_STORE_RESONSE_DTO1);

        // when - then
        ResultActions resultActions  = mvc.perform(put("/api/stores/{storeId}",1)
                .content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );

        resultActions.andExpect(content().json("{\"statusCode\":400,\"state\":\"BAD_REQUEST\",\"message\":\"Access is denied\"}"));

    }

    @Test
    @DisplayName("음식점 수정 권한 테스트 - 관리자 불가 ")
    void updateAuthError2Test() throws Exception {

        // given
        this.mockAdminSetup();
        String postInfo = objectMapper.writeValueAsString(TEST_STORE_REQUEST_DTO);

        given(storeService.updateStore(1L,TEST_STORE_REQUEST_DTO,TEST_SELLER)).willReturn(TEST_STORE_RESONSE_DTO1);

        // when - then
        ResultActions resultActions  = mvc.perform(put("/api/stores/{storeId}",1)
                .content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );

        resultActions.andExpect(content().json("{\"statusCode\":400,\"state\":\"BAD_REQUEST\",\"message\":\"Access is denied\"}"));

    }


    @Test
    @DisplayName("음식점 삭제 테스트")
    void deleteTest() throws Exception {

        // given
        this.mockSellerSetup();

        given(storeService.deleteStore(1L,TEST_SELLER)).willReturn("피자");

        // when - then
        ResultActions resultActions  = mvc.perform(delete("/api/stores/{storeId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );

        resultActions.andExpect(status().isOk()).andReturn();

        resultActions.andExpect(content().json("{\"httpStatus\":200,\"message\":\"해당 음식점 삭제를 성공했습니다. 음식점 : 피자\",\"data\":null}"));

    }

    @Test
    @DisplayName("음식점 삭제 테스트 - 일반 유저 불가 ")
    void deleteAuthErrorTest() throws Exception {

        // given
        this.mockUserSetup();

        given(storeService.deleteStore(1L,TEST_SELLER)).willReturn("피자");

        // when - then
        ResultActions resultActions  = mvc.perform(delete("/api/stores/{storeId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal)
        );

        resultActions.andExpect(content().json("{\"statusCode\":400,\"state\":\"BAD_REQUEST\",\"message\":\"Access is denied\"}"));

    }


}