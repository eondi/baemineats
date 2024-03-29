package com.sparta.baemineats.repository;

import com.sparta.baemineats.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like,Long> {

    List<Like> findByUser_UserId(Long userId);

    List<Like> findALLByStore_StoreId(Long storeId);

    List<Like> findALLByUser_UserId(Long storeId);

}
