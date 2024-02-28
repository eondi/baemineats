package com.sparta.baemineats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.baemineats.config.WebSecurityConfig;
import com.sparta.baemineats.dto.requestDto.LoginRequestDto;
import com.sparta.baemineats.dto.requestDto.SignupRequestDto;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.entity.UserRoleEnum;
import com.sparta.baemineats.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static com.sparta.baemineats.entity.UserRoleEnum.USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class,
excludeFilters = {
@ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = WebSecurityConfig.class
)
        })
@DisabledInAotMode
class UserControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Spy
    PasswordEncoder passwordEncoder;



    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    @Test
    @DisplayName(" UserController signup 정상적으로 회원가입 테스트")
    void test1() throws Exception {
        //given
        SignupRequestDto requestDto = new SignupRequestDto("bob4","aA12345@","aasdfas@naver.com","어쩔아파트 저쩔동 어쩔호", USER);

        //when
        mvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
        )
                .andExpect(status().isOk());

        verify(userService, times(1)).signup(any(SignupRequestDto.class));

    }

    @Test
    @DisplayName("회원가입된 유저가 로그인 테스트")
    void test2() throws Exception {
        // Given
       User user = new User("bob4",passwordEncoder.encode("aAa123456@"), "어쩔아파트 저쩔 동 이쩔호","asdfsadf@naver.com");

      when(userService.findUserByUsername(eq("bob4"))).thenReturn(user);
        LoginRequestDto request = new LoginRequestDto("bob4","aAa123456@");

        // When/Then
        mvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("로그인 성공"));


        verify(userService, times(1)).login(any(LoginRequestDto.class), any(HttpServletResponse.class));
    }

    @Test
    @DisplayName("로그인된 유저 로그아웃 테스트")
    void test3() {

    }




}