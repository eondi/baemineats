package com.sparta.baemineats.controller;

import com.sparta.baemineats.dto.requestDto.MenuRequest;
import com.sparta.baemineats.dto.responseDto.MenuResponse;
import com.sparta.baemineats.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/stores/{storeId}")
    @Operation(summary = "메뉴 등록", description = "메뉴를 등록한다")
    public MenuResponse createMenu(@PathVariable Long storeId, @RequestBody MenuRequest requestDto) {
        return menuService.createMenu(storeId, requestDto);
    }

    @GetMapping("")
    @Operation(summary = "전체 메뉴 조회", description = "전체 메뉴를 조회한다")
    public List<MenuResponse> getMenus() {
        return menuService.getMenus();
    }

    @GetMapping("/{menuId}")
    @Operation(summary = "단일 메뉴 조회", description = "단일 메뉴를 조회한다")
    public List<MenuResponse> getMenuFindById(@PathVariable Long menuId) {
        return menuService.getMenuFindById(menuId);
    }

    @PutMapping("/{menuId}")
    @Operation(summary = "메뉴 수정", description = "메뉴를 수정한다")
    public Long updateMenu(@PathVariable Long menuId, @RequestBody MenuRequest requestDto) {
        return menuService.updateMenu(menuId, requestDto);
    }

    @DeleteMapping("/{menuId}")
    @Operation(summary = "메뉴 삭제", description = "메뉴를 삭제한다")
    public Long deleteMenu(@PathVariable Long menuId) {
        return menuService.deleteMenu(menuId);
    }
}
