package com.sparta.baemineats.repository;

import com.sparta.baemineats.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
