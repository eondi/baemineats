package com.sparta.baemineats.repository;

import com.sparta.baemineats.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
}
