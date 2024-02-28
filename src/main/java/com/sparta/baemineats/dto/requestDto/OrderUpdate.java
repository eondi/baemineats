package com.sparta.baemineats.dto.requestDto;

import com.sparta.baemineats.entity.Order.OrderStateEnum;

public class OrderUpdate {
    private OrderStateEnum orderState;

    public OrderUpdate() {
    }

    public OrderUpdate(OrderStateEnum orderState) {
        this.orderState = orderState;
    }

    public OrderStateEnum getOrderState() {
        return this.orderState;
    }

}
