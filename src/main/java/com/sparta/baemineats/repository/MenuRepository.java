package com.sparta.baemineats.repository;

import com.sparta.baemineats.dto.responseDto.MenuResponse;
import com.sparta.baemineats.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findMenuByMenuId(Long menuId);
}
