package com.sparta.baemineats.dto.responseDto;

import com.sparta.baemineats.entity.Order;
import lombok.Getter;

@Getter
public class OrderResponse {
    private Long orderId;
    private Long storeId;
    private Long userId;
    private boolean orderComplete;
    private String orderState;

    public OrderResponse(Order order) {
        this.orderId = order.getOrderId();
        this.storeId = order.getStore().getStoreId();
        this.userId = order.getUser().getUserId();
        this.orderComplete = order.isOrderComplete();
        this.orderState = order.getOrderState();
    }
}