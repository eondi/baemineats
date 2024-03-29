package com.sparta.baemineats.controller;

import com.sparta.baemineats.dto.requestDto.MenuRequest;
import com.sparta.baemineats.dto.responseDto.MenuResponse;
import com.sparta.baemineats.dto.responseDto.ResponseForm;
import com.sparta.baemineats.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/{storeId}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Operation(summary = "메뉴 등록", description = "메뉴를 등록한다")
    public ResponseEntity<ResponseForm> createMenu(@PathVariable Long storeId, @ModelAttribute MenuRequest requestDto) {
        menuService.createMenu(storeId, requestDto);
        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("메뉴를 정상적으로 등록하였습니다")
                        .build());
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
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Operation(summary = "메뉴 수정", description = "메뉴를 수정한다")
    public ResponseEntity<ResponseForm> updateMenu(@PathVariable Long menuId, @ModelAttribute MenuRequest requestDto) {
        menuService.updateMenu(menuId, requestDto);
        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("메뉴를 정상적으로 수정하였습니다")
                        .build());
    }

    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    @Operation(summary = "메뉴 삭제", description = "메뉴를 삭제한다")
    public ResponseEntity<ResponseForm> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("메뉴를 정상적으로 삭제하였습니다")
                        .build());
    }
}
