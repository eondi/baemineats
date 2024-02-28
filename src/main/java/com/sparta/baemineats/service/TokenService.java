package com.sparta.baemineats.service;

import com.sparta.baemineats.entity.Token;
import com.sparta.baemineats.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    @Transactional
    public void saveToken(String username, String jwtToken){
        tokenRepository.save(new Token(username,jwtToken));
    }

    public Token findTokenByUsername(String username) {
      return  tokenRepository.findByUsername(username).orElseThrow(
                ()->  new RuntimeException("예상치 못한 예외가 발생했습니다.")
                );
    }

}
