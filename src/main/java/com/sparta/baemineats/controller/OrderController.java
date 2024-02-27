package com.sparta.baemineats.controller;

import com.sparta.baemineats.dto.requestDto.OrderRequest;
import com.sparta.baemineats.dto.responseDto.OrderResponse;
import com.sparta.baemineats.dto.responseDto.ResponseForm;
import com.sparta.baemineats.entity.Order;
import com.sparta.baemineats.security.UserDetailsImpl;
import com.sparta.baemineats.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("")
    public ResponseEntity<ResponseForm> createOrder(
            @RequestBody OrderRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        orderService.createOrder(request, userDetails.getUser());
        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                    .httpStatus(HttpStatus.OK.value())
                    .message("주문 생성")
                    .build());
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ResponseForm> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        orderService.cancelOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("주문 취소")
                        .build());
    }

    @GetMapping("")
    public List<OrderResponse> getOrders() {
        return orderService.getOrders();
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<ResponseForm> completeOrder(@PathVariable Long orderId) {
        orderService.completeOrder(orderId);
        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("주문 완료")
                        .build());
    }

    @PutMapping("/state/{orderId}")
    public ResponseEntity<ResponseForm> updateOrderState(
            @PathVariable Long orderId,
            @RequestBody String newState,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        orderService.updateOrderState(orderId, newState, userDetails.getUser());
        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("주문 상태 변경")
                        .build());

    }
}
