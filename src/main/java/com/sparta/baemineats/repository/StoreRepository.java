package com.sparta.baemineats.repository;

import com.sparta.baemineats.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store,Long> {
    Store findBySellerName(String username);
}
