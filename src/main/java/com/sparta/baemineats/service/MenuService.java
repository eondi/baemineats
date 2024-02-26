package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.MenuRequest;
import com.sparta.baemineats.dto.responseDto.MenuResponse;
import com.sparta.baemineats.entity.Menu;
import com.sparta.baemineats.entity.Store;
import com.sparta.baemineats.repository.MenuRepository;
import com.sparta.baemineats.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public MenuResponse createMenu(Long storeId, MenuRequest requestDto) {
        Store store = findStoreId(storeId);
        Menu menu = menuRepository.save(new Menu(requestDto, store));
        return new MenuResponse(menu);
    }

    public List<MenuResponse> getMenus() {
        return menuRepository.findAll().stream().map(MenuResponse::new).toList();
    }

    public List<MenuResponse> getMenuFindById(Long menuId) {
        return menuRepository.findMenuByMenuId(menuId).stream().map(MenuResponse::new).toList();
    }

    @Transactional
    public Long updateMenu(Long menuId, MenuRequest requestDto) {
        Menu menu = findMenu(menuId);

        menu.update(requestDto);

        menuRepository.save(menu);

        return menuId;
    }

    @Transactional
    public Long deleteMenu(Long menuId) {
        Menu menu = findMenu(menuId);

        menuRepository.delete(menu);

        return menuId;
    }

    private Store findStoreId(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() ->
                new IllegalArgumentException("선택한 가게는 존재하지 않습니다."));
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(() ->
                new IllegalArgumentException("선택한 메뉴는 존재하지 않습니다."));
    }
}
