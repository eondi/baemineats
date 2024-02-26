package com.sparta.baemineats.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    private Long storeId;
    private Long userId;
    private Long menuId;
    private String orderState;
}