package com.sparta.baemineats.controller;

import com.sparta.baemineats.dto.responseDto.LikeStoreResponse;
import com.sparta.baemineats.dto.responseDto.LikeUserResponse;
import com.sparta.baemineats.dto.responseDto.ResponseForm;
import com.sparta.baemineats.security.UserDetailsImpl;
import com.sparta.baemineats.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @GetMapping("{storeId}")
    @Operation(summary = "음식점 좋아요", description = "특정 음식점에 좋아요를 할수있다 (찜)")
    public ResponseEntity<ResponseForm> likeStore(@PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String response = likeService.likeStore(storeId, userDetails.getUser());

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message(" 음식점 :" + response + "에 좋아요를 성공했습니다.")
                        .build());
    }

    @DeleteMapping("{storeId}")
    @Operation(summary = "음식점 좋아요취소", description = "좋아요를 취소 할 수 있다")
    public ResponseEntity<ResponseForm> unlikeStore(@PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String response = likeService.unlikeStore(storeId, userDetails.getUser());

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message(" 음식점 :" + response + "에 좋아요를 취소했습니다.")
                        .build());
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "좋아요 유저 조회", description = "음식점에 좋아요를 누른 사용자를 조회할수있다")
    public ResponseEntity<ResponseForm> getUserlike(@PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<LikeUserResponse> response = likeService.getUserlike(storeId, userDetails.getUser());

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("좋아요한 유저 정보 조회 성공 ")
                        .data(response)
                        .build());
    }

    @GetMapping("/user")
    @Operation(summary = "좋아요 가게 조회", description = "자신이 좋아요한 가게 정보 조회")
    public ResponseEntity<ResponseForm> getStorelike( @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<LikeStoreResponse> response = likeService.getStorelike(userDetails.getUser());

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("좋아요 가게 정보 조회 성공")
                        .data(response)
                        .build());
    }

    @GetMapping("/count/{storeId}")
    @Operation(summary = "좋아요 가게 조회", description = "가게 좋아요 갯수 조회")
    public ResponseEntity<ResponseForm> getlikeCount(@PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        int response = likeService.getlikeCount(storeId, userDetails.getUser());

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("좋아요 수 : " + response)
                        .build());
    }


}
