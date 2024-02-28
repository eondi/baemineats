package com.sparta.baemineats.repository;

import com.sparta.baemineats.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Long> {
    List<Cart> findAllByUser_UserId(Long userId);
    void deleteAllByUser_UserId(Long userId);
}
