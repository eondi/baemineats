package com.sparta.baemineats.controller;

import com.sparta.baemineats.dto.requestDto.OrderUpdate;
import com.sparta.baemineats.dto.responseDto.OrderResponse;
import com.sparta.baemineats.dto.responseDto.ResponseForm;
import com.sparta.baemineats.entity.Order;
import com.sparta.baemineats.security.UserDetailsImpl;
import com.sparta.baemineats.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

//    @PostMapping("")
//    public ResponseEntity<ResponseForm> createOrder(
//            @RequestBody OrderRequest request,
//            @AuthenticationPrincipal UserDetailsImpl userDetails
//    ) {
//        OrderResponse orderResponse = orderService.createOrder(request, userDetails.getUser());
//        return ResponseEntity.ok()
//                .body(ResponseForm.builder()
//                    .httpStatus(HttpStatus.OK.value())
//                    .message("주문 생성")
//                    .build());
//    }

@PreAuthorize("hasRole('ROLE_USER')")
@PostMapping("")
public ResponseEntity<ResponseForm> createOrderFromCart(
        @AuthenticationPrincipal UserDetailsImpl userDetails
) {
    List<Order> res = orderService.createOrderFromCart(userDetails.getUser());
    return ResponseEntity.ok()
            .body(ResponseForm.builder()
                    .httpStatus(HttpStatus.OK.value())
                    .message("주문 생성")
                    .data(res)
                    .build());
}

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_SELLER')")
    @GetMapping("")
    public List<OrderResponse> getOrders(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return orderService.getOrders(userDetails);
    }

    @PreAuthorize("hasRole('ROLE_SELLER')")
    @PutMapping("/state/{orderId}")
    public ResponseEntity<ResponseForm> updateOrderState(
            @PathVariable Long orderId,
            @RequestBody OrderUpdate orderUpdate,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        orderService.updateOrderState(orderId, orderUpdate, userDetails.getUser());
        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("주문 상태 변경")
                        .build());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ResponseForm> deleteOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        orderService.deleteOrder(orderId, userDetails.getUser());
        return ResponseEntity.ok()
                .body(ResponseForm.builder()
                        .httpStatus(HttpStatus.OK.value())
                        .message("주문 취소")
                        .build());
    }
}
