package com.sparta.baemineats.controller;

import com.sparta.baemineats.dto.requestDto.ReviewRequest;
import com.sparta.baemineats.dto.responseDto.ResponseForm;
import com.sparta.baemineats.dto.responseDto.ReviewResponse;
import com.sparta.baemineats.security.UserDetailsImpl;
import com.sparta.baemineats.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class reviewController {


    private final ReviewService reviewService;

    @PostMapping("/{orderId}")
    @Operation(summary = "리뷰 등록", description = "리뷰를 등록한다")
    public ResponseEntity<ResponseForm> createReview(@PathVariable Long orderId, @RequestBody ReviewRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
         ReviewResponse response = reviewService.createReview(orderId, request, userDetails.getUser());

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("리뷰가 등록되었습니다")
                        .data(response)
                        .build());
    }

//    @GetMapping("/{orderid}")
//    @Operation(summary = "음식점 리뷰 조회", description = "음식점 리뷰 전체를 조회 한다")
//    public ResponseEntity<ResponseForm> getStoreReview(@PathVariable Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        List<Review> response = reviewService.getStoreReview(orderId, userDetails.getUser());
//
//        return ResponseEntity.ok()
//                .body(ResponseForm.builder()
//                        .httpStatus(HttpStatus.OK.value())
//                        .message("리뷰가 등록되었습니다")
//                        .data(response)
//                        .build());
//    }
//
    @PutMapping("{reviewId}")
    @Operation(summary = "음식점 수정", description = "특정 음식점을 수정 한다")
    public ResponseEntity<ResponseForm> updateStore(@PathVariable Long reviewId, @RequestBody ReviewRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewResponse response = reviewService.updateReview(reviewId, request,userDetails.getUser());

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("리뷰가 수정되었습니다")
                        .data(response)
                        .build());
    }

    @DeleteMapping("{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "특정 리뷰를 삭제 한다")
    public ResponseEntity<ResponseForm> deleteStore(@PathVariable Long reviewId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String response = reviewService.deleteStore(reviewId, userDetails.getUser());

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("리뷰가 삭제되었습니다")
                        .data(response)
                        .build());
    }



}
