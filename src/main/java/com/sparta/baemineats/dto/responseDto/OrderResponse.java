package com.sparta.baemineats.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {
    private Long orderId;
    private Long storeId;
    private Long userId;
    private boolean orderComplete;
    private String orderState;
}
