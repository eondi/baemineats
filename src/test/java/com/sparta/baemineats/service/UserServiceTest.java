package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.LoginRequestDto;
import com.sparta.baemineats.dto.requestDto.SignupRequestDto;
import com.sparta.baemineats.dto.responseDto.TokenDto;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.entity.UserRoleEnum;
import com.sparta.baemineats.jwt.JwtUtil;
import com.sparta.baemineats.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Spy
    PasswordEncoder passwordEncoder;

    @Mock
    TokenService tokenService;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("정상적인 회원가입")
    void test1() {
        //given
        String username = "bob4";
        String password = "aA11223344";
        String email = "astd444@naver.com";
        String address = "이쪽동 저쩌고아파트 쩌쪽동 오늘호";
        UserRoleEnum userRoleEnum = UserRoleEnum.USER;

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);

        //when
        SignupRequestDto requestDto = new SignupRequestDto(username, password, email, address, userRoleEnum);
        userService.signup(requestDto);

        //then
        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    @DisplayName("정상적인 로그인")
    void test2() throws Exception{
    //given
        HttpServletResponse response = new MockHttpServletResponse();
        LoginRequestDto requestDto = new LoginRequestDto("bob4", "aA11223344");
        User user = new User("bob4", passwordEncoder.encode(requestDto.getPassword()),"어쩌고동 어쩌아파트 어쩔동 어쩔호" ,"alstkd44@naver.com");

        when(userRepository.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestDto.getPassword(),user.getPassword())).thenReturn(true);

        TokenDto tokenDto = new TokenDto("Barer", "Authorization", "accessToken",3600L);
        when(jwtUtil.createToken(user.getUsername(), user.getRole())).thenReturn(tokenDto);

        //when
        userService.login(requestDto,response);

        //then
        verify(tokenService, times(1)).saveToken(anyString(), anyString());
        verify(jwtUtil, times(1)).accessTokenSetHeader(anyString(), eq(response));


    }

}