package com.sparta.baemineats.dto.responseDto;

import com.sparta.baemineats.entity.Review;
import lombok.Getter;

@Getter
public class StoreReviewResponse {

    private String menuName;
    private String content;
    private double rate;


    public StoreReviewResponse(Review review) {
        this.menuName = review.getMenu().getMenuName();
        this.content = review.getContent();
        this.rate = review.getRate();
    }
}
