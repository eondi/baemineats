package com.sparta.baemineats.dto.requestDto;

import com.sparta.baemineats.entity.Order.OrderStateEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderUpdate {
    private OrderStateEnum orderState;

    public OrderUpdate(OrderStateEnum orderState) {
        this.orderState = orderState;
    }

    public OrderStateEnum getOrderState() {
        return this.orderState;
    }

}
