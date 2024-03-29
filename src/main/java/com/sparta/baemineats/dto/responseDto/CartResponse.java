package com.sparta.baemineats.dto.responseDto;

import com.sparta.baemineats.entity.Cart;
import lombok.Getter;
import org.springframework.transaction.annotation.Transactional;

@Getter
public class CartResponse {
    private String storeName;
    private Long menuId;
    private String menuName;
    private Long quantity;
    private int totalPrice;
    public CartResponse(Cart cart) {
        this.storeName = cart.getStore().getStoreName();
        this.menuId = cart.getMenu().getMenuId();
        this.menuName = cart.getMenu().getMenuName();
        this.quantity = cart.getQuantity();
        this.totalPrice = cart.getTotalPrice();
    }
}
