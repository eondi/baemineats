package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.SignupRequestDto;
import com.sparta.baemineats.dto.requestDto.UserModifyAllRequestDto;
import com.sparta.baemineats.dto.requestDto.UserModifyPasswordRequestDto;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.entity.UserRoleEnum;
import com.sparta.baemineats.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDto requestDto) {
        String username= requestDto.getUserName();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String address = requestDto.getAddress();
        String email = requestDto.getEmail();
        UserRoleEnum role = requestDto.getUserRoleEnum();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new DataIntegrityViolationException("중복된 username 입니다.");
        }

        // 이메일 중복 확인
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new DataIntegrityViolationException("이미 사용중인 email 입니다.");
        }

        // 사용자 등록
        User user = new User(username, password, address, email, role);
        userRepository.save(user);
    }

    @Transactional
    public void updateAll(User user, UserModifyAllRequestDto requestDto) {

        User findUser = findUserByUsername(user);

        findUser.userProfileAllUpdate(requestDto);
    }

    @Transactional
    public void updatePassword(User user, UserModifyPasswordRequestDto requestDto) {

        User findUser= findUserByUsername(user);

        if(passwordEncoder.matches(requestDto.getNewPassword(), findUser.getPassword())){
            throw new AccessDeniedException("비밀번호가 일치하지 않습니다.");
        }

        if(requestDto.getNewPassword().equals(requestDto.getPassword())){
            throw new DataIntegrityViolationException("이전 비밀번호와 일치합니다.");
        }

        findUser.updatePassword(passwordEncoder.encode(requestDto.getNewPassword()));

    }

    private User findUserByUsername(User user) {
        return userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new NoSuchElementException("유저가 존재하지 않습니다.")
        );
    }
}
