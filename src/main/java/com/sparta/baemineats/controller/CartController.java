package com.sparta.baemineats.controller;

import com.sparta.baemineats.dto.requestDto.CartRequest;
import com.sparta.baemineats.dto.responseDto.CartResponse;
import com.sparta.baemineats.dto.responseDto.ResponseForm;
import com.sparta.baemineats.entity.Menu;
import com.sparta.baemineats.security.UserDetailsImpl;
import com.sparta.baemineats.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ResponseForm> addToCart(
            @RequestBody CartRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            List<Menu> menus) {
        cartService.addToCart(request, userDetails.getUser(), menus);
        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("장바구니 추가")
                        .build());
    }

    @GetMapping
    public List<CartResponse> getCarts(){
            return cartService.getCarts();
    }

    @DeleteMapping("{cartId}")
    public ResponseEntity<ResponseForm> deleteCart(
            @RequestBody Long cartId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        cartService.deleteCart(cartId, userDetails.getUser());
        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("장바구니 삭제")
                        .build());
    }

}
