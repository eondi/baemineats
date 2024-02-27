package com.sparta.baemineats.dto.requestDto;

import lombok.Getter;
import org.springframework.web.bind.annotation.PostMapping;

@Getter
public class ReviewRequest {
    private Long orderId;
    private Long cartId;
    private Long storeId;
    private Long menuId;

    private String content;
    private double rate;
}
