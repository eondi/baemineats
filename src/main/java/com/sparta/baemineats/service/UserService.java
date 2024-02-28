package com.sparta.baemineats.service;


import com.sparta.baemineats.dto.requestDto.LoginRequestDto;
import com.sparta.baemineats.dto.requestDto.SignupRequestDto;
import com.sparta.baemineats.dto.requestDto.UserModifyAllRequestDto;
import com.sparta.baemineats.dto.requestDto.UserModifyPasswordRequestDto;
import com.sparta.baemineats.dto.responseDto.ResponseUserList;
import com.sparta.baemineats.dto.responseDto.TokenDto;
import com.sparta.baemineats.entity.Token;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.jwt.JwtUtil;
import com.sparta.baemineats.repository.TokenRepository;
import com.sparta.baemineats.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    @Transactional
    public void signup(SignupRequestDto requestDto) {
        String username= requestDto.getUserName();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();

        // 회원 중복 확인
        if (userRepository.existsByUsername(username)){
            throw new DataIntegrityViolationException("중복된 username 입니다.");
        }

        // 이메일 중복 확인

        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("이미 사용중인 email 입니다.");
        }

        // 사용자 등록
        User user = new User(requestDto,password);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<ResponseUserList> findAllUser() {

        return userRepository.findAll().stream()
                .map(ResponseUserList::new)
                .toList();
    }

    @Transactional
    public void updateAll(User user, UserModifyAllRequestDto requestDto) {

        User findUser = findUserByUsername(user.getUsername());

        findUser.userProfileAllUpdate(requestDto);

    }

    @Transactional
    public void updatePassword(User user, UserModifyPasswordRequestDto requestDto) {

        User findUser= findUserByUsername(user.getUsername());

        if(passwordEncoder.matches(requestDto.getNewPassword(), findUser.getPassword())){
            throw new AccessDeniedException("비밀번호가 일치하지 않습니다.");
        }

        if(requestDto.getNewPassword().equals(requestDto.getPassword())){
            throw new DataIntegrityViolationException("이전 비밀번호와 일치합니다.");
        }

        findUser.updatePassword(passwordEncoder.encode(requestDto.getNewPassword()));


    }

    @Transactional
    public void deActiveUser(Long userId){
        User findUser = findUserByUserId(userId);

        findUser.deActiveUser();


    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchElementException("유저가 존재하지 않습니다.")
        );
    }

    private User findUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("유저가 존재하지 않습니다.")
        );
    }


    @Transactional
    public void login(LoginRequestDto requestDto,
                      HttpServletResponse response) throws Exception {
        User user = findUserByUsername(requestDto.getUsername());

        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new LoginException("비밀번호가 일치하지 않습니다.");
        }
        TokenDto tokenDto = jwtUtil.createToken(user.getUsername(),user.getRole());
        String accessToken = tokenDto.getAccessToken();
        tokenService.saveToken(user.getUsername(),accessToken);
        jwtUtil.accessTokenSetHeader(accessToken,response);


    }

    @Transactional
    public void logout(HttpServletRequest request) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Token token = tokenService.findTokenByUsername(user.getUsername());
        if(!token.getToken().isEmpty() && token.getToken().equals(jwtUtil.getJwtFromHeader(request))){
            tokenRepository.delete(token);
        }
    }
}
