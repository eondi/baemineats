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
    private final UploadService uploadService;
    private final StoreRepository storeRepository;

    @Transactional
    public void createMenu(Long storeId, MenuRequest requestDto) {

        String imageUrl = uploadService.uploadImageAndGetUrl(requestDto.getImage());
        Store store = findStoreId(storeId);
        menuRepository.save(new Menu(requestDto, store, imageUrl));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> getMenus() {
        return menuRepository.findAll().stream().map(menu -> new MenuResponse(menu, menu.getImageUrl())).toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> getMenuFindById(Long menuId) {
        return menuRepository.findMenuByMenuId(menuId).stream().map(menu -> new MenuResponse(menu, menu.getImageUrl())).toList();
    }

    @Transactional
    public void updateMenu(Long menuId, MenuRequest requestDto) {

        Menu menu = findMenu(menuId);
        String imageUrl = uploadService.uploadImageAndGetUrl(requestDto.getImage());

        menu.update(requestDto, imageUrl);
        menuRepository.save(menu);
    }

    @Transactional
    public void deleteMenu(Long menuId) {

        Menu menu = findMenu(menuId);

        menuRepository.delete(menu);
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
