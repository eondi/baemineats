package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.responseDto.OrderResponse;
import com.sparta.baemineats.entity.*;
import com.sparta.baemineats.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final CartRepository cartRepository;


//    @Transactional
//    public OrderResponse createOrder(OrderRequest request, User user){
//
//        userRepository.findById(request.getUserId()).orElseThrow(()
//                -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
//
//        Store store = storeRepository.findById(request.getStoreId()).orElseThrow(()
//                -> new IllegalArgumentException("음식점이 존재하지 않습니다."));
//
//        Menu menu = menuRepository.findById(request.getMenuId()).orElseThrow(()
//                -> new IllegalArgumentException("메뉴 존재하지 않습니다."));
//
//        orderRepository.save(new Order(request, user, store, menu));
//        //
//        return new OrderResponse();
//    }

    @Transactional
    public List<Order> createOrderFromCart(User user) {
        List<Cart> cartList = cartRepository.findAllByUser_UserId(user.getUserId());

        if (cartList.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있습니다.");
        }

        List<Order> newOrders = new ArrayList<>();
        // 장바구니의 각 메뉴를 주문으로 변환
        for (Cart cart : cartList) {
            Menu menu = cart.getMenu();
            Store store = cart.getStore(); // 메뉴에 대한 상점 정보가 필요하다고 가정
            Order order = new Order(user, store, menu);
            newOrders.add(order);
        }

        orderRepository.saveAll(newOrders);

        // 장바구니 비우기
        cartRepository.deleteAllByUser_UserId(user.getUserId());

        // 새로 생성된 주문들의 정보 반환
//        return newOrders.stream()
//                .map(OrderResponse::new).toList();

        return newOrders;
    }


    @Transactional
    public List<OrderResponse> getOrders(){
        return orderRepository.findAll().stream().map(OrderResponse::new).toList();

    }

//    @Transactional
//    public void completeOrder(Long orderId){
//        Order order = orderRepository.findById(orderId).orElseThrow(()
//                -> new IllegalArgumentException("주문이 존재하지 않습니다."));
//        order.setOrderState("주문완료");
//        order.setOrderComplete(true);
//        order.setConfirmTime(LocalDateTime.now());
//        orderRepository.save(order);
//    }

    @Transactional
    public void updateOrderState(Long orderId, String newState, User user){
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        if (!order.getUser().equals(user)) {
            throw new IllegalArgumentException("수정할 수 있습니다.");
        }
        order.setOrderState(newState);
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId, User user){
        Order order = findOne(orderId);

        if (!order.getUser().equals(user)) {
            throw new IllegalArgumentException("주문자만 취소할 수 있습니다.");
        }
        orderRepository.delete(order);
    }

    private Order findOne(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()
                -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }

}