package com.sparta.baemineats.controller;

import com.sparta.baemineats.dto.MenuResponse;
import com.sparta.baemineats.dto.requestDto.MenuRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {


    @PostMapping()
    @Operation(summary = "메뉴 추가", description = "메뉴를 추가한다")
    public MenuResponse createMenu(@PathVariable Long menuId, @RequestBody MenuRequest request) {
        return null;
    }
}
