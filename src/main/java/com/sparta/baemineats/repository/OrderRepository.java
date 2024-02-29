package com.sparta.baemineats.repository;

import com.sparta.baemineats.entity.Order;
import com.sparta.baemineats.entity.Store;
import com.sparta.baemineats.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByStore(Store store);
}
