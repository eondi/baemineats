package com.sparta.baemineats.repository;

import com.sparta.baemineats.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Optional<Object> findByStore_StoreId(Long storeId);

    Optional<Object> findByUser_UserId(Long userId);
}
