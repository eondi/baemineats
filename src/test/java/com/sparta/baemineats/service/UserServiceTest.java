package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.SignupRequestDto;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.entity.UserRoleEnum;
import com.sparta.baemineats.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Spy
    PasswordEncoder passwordEncoder;

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
        UserRoleEnum userRoleEnum = UserRoleEnum.valueOf("USER");

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

}