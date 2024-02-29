package com.sparta.baemineats.repository;

import com.sparta.baemineats.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByStore_StoreId(Long storeId);
}
