package com.sparta.baemineats.dto.responseDto;

import com.sparta.baemineats.entity.Review;
import lombok.Getter;

@Getter
public class ReviewResponse {

    private String content;
    private double rate;


    public ReviewResponse(Review review) {
        this.content = review.getContent();
        this.rate = review.getRate();
    }
}
