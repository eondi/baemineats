package com.sparta.baemineats.dto.requestDto;

import lombok.Getter;

@Getter
public class CartRequest {
    private Long storeId;
    private Long userId;
    private Long menuId;
    private Long quantity;
    private int totalPrice;
}
