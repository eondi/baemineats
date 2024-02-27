package com.sparta.baemineats.dto.responseDto;

import com.sparta.baemineats.entity.Cart;
import com.sparta.baemineats.entity.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CartResponse {
    private Long storeId;
    private List<Menu> menus;
    private Long quantity;
    private int totalPrice;

    public CartResponse(Cart cart) {
        this.storeId = cart.getStore().getStoreId();
        this.menus = cart.getMenus();
        this.quantity = cart.getQuantity();
        this.totalPrice = cart.getTotalPrice();
    }
}
