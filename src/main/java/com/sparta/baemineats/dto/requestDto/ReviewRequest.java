package com.sparta.baemineats.dto.requestDto;

import lombok.Getter;

@Getter
public class ReviewRequest {
    private Long orderId;
    private Long cartId;
    private Long storeId;
    private Long menuId;

    private String content;
    private double rate;
}
