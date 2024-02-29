package com.sparta.baemineats.dto.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartRequest {
    private Long storeId;
    private Long userId;
    private Long menuId;
    private Long quantity;
    private int totalPrice;

    public CartRequest(Long storeId, Long userId, Long menuId, Long quantity, int totalPrice) {
        this.storeId = storeId;
        this.userId = userId;
        this.menuId =  menuId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
