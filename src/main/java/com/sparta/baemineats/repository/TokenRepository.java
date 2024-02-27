package com.sparta.baemineats.repository;

import com.sparta.baemineats.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByUsername(String username);

    boolean existsByToken(String token);
}
