package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.LoginRequestDto;
import com.sparta.baemineats.dto.requestDto.SignupRequestDto;
import com.sparta.baemineats.dto.responseDto.ResponseUserList;
import com.sparta.baemineats.dto.responseDto.TokenDto;
import com.sparta.baemineats.entity.Token;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.entity.UserRoleEnum;
import com.sparta.baemineats.jwt.JwtUtil;
import com.sparta.baemineats.repository.TokenRepository;
import com.sparta.baemineats.repository.UserRepository;
import com.sparta.baemineats.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAndTokenServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    TokenRepository tokenRepository;
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

    @Test
    @DisplayName("정상적인 로그아웃")
    void test3(){
        //given
        User user = new User(
                "bob4",
                passwordEncoder.encode("aA11111234"),
                "어쩌고동 어쩌고 저쩌고 이러한 호",
                "asdf2345@naver.com"
        );
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(jwtUtil.getJwtFromHeader(request)).thenReturn("testJwt");

        Token token = new Token(userDetails.getUsername(),"testJwt");
        when(tokenService.findTokenByUsername(userDetails.getUsername())).thenReturn(token);

        //when
        userService.logout(request);

        //then
        verify(tokenRepository, times(1)).delete(token);

    }

    @Test
    @DisplayName("사용자 전체 조회 테스트")
    void test4(){
        //given
        List<User> userList = List.of(
                new User("user1", "password1","어쩌고동 어쩌고 호","asdfea@naver.com"),
                new User("user2", "password2","어쩔동어쩔아파트 저쩔동 이쩔호","aedfeg@naver.com")
        );
        when(userRepository.findAll()).thenReturn(userList);

        // WHEN
        List<ResponseUserList> result = userService.findAllUser();

        //then
        assertEquals(userList.size(), result.size());
        verify(userRepository, times(1)).findAll();

    }





}