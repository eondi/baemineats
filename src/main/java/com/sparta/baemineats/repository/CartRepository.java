package com.sparta.baemineats.repository;

import com.sparta.baemineats.entity.Cart;
import com.sparta.baemineats.entity.Menu;
import com.sparta.baemineats.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
