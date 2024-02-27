package com.sparta.baemineats.controller;

import com.sparta.baemineats.dto.requestDto.StoreRequest;
import com.sparta.baemineats.dto.responseDto.ResponseForm;
import com.sparta.baemineats.dto.responseDto.StoreResponse;
import com.sparta.baemineats.repository.StoreRepository;
import com.sparta.baemineats.security.UserDetailsImpl;
import com.sparta.baemineats.service.StoreService;
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
@RequestMapping("api/stores")
public class StoreController {
    private final StoreRepository storeRepository;

    private final StoreService storeService;

    @PostMapping
    @Operation(summary = "음식점 등록", description = "음식점을 등록한다")
    public ResponseEntity<ResponseForm> createStore(@RequestBody StoreRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        StoreResponse response = storeService.createStore(request, userDetails.getUser());

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("정상적으로 가게가 등록되었습니다")
                        .data(response)
                        .build());
    }

    @GetMapping("/all")
    @Operation(summary = "음식점 목록 전체 조회", description = "음식점 전체 목록 조회를 한다")
    public ResponseEntity<ResponseForm> getStoreNames(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<StoreResponse> response = storeService.getStoreAll();

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("전체 목록 조회에 성공했습니다.")
                        .data(response)
                        .build());
    }

    @GetMapping("/{storeId}")
    @Operation(summary = "음식점 상세 조회", description = "특정 음식점 상세 조회를 한다")
    public ResponseEntity<ResponseForm> getStore(@PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreResponse response = storeService.getStore(storeId, userDetails.getUser());

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("해당 음식점 상세 조회에 성공했습니다.")
                        .data(response)
                        .build());
    }

    @PutMapping("{storeId}")
    @Operation(summary = "음식점 수정", description = "특정 음식점을 수정 한다")
    public ResponseEntity<ResponseForm> updateStore(@PathVariable Long storeId, @RequestBody StoreRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StoreResponse response = storeService.updateStore(storeId, request, userDetails.getUser());

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("해당 음식점 수정을 성공했습니다.")
                        .data(response)
                        .build());
    }

    @DeleteMapping("{storeId}")
    @Operation(summary = "음식점 삭제", description = "특정 음식점을 삭제 한다")
    public ResponseEntity<ResponseForm> deleteStore(@PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String response = storeService.deleteStore(storeId, userDetails.getUser());

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("해당 음식점 삭제를 성공했습니다. 음식점 : " + response)
                        .build());
    }

    @GetMapping("/like/{storeId}")
    @Operation(summary = "음식점 좋아요", description = "특정 음식점에 좋아요를 할수있다 (찜)")
    public ResponseEntity<ResponseForm> likeStore(@PathVariable Long storeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String response = storeService.likeStore(storeId, userDetails.getUser());

        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message(" 음식점 :" + response + " 에 좋아요를 성공했습니다.")
                        .build());
    }


}
