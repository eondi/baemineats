package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.OrderRequest;
import com.sparta.baemineats.dto.responseDto.OrderResponse;
import com.sparta.baemineats.entity.Menu;
import com.sparta.baemineats.entity.Order;
import com.sparta.baemineats.entity.Store;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.repository.MenuRepository;
import com.sparta.baemineats.repository.OrderRepository;
import com.sparta.baemineats.repository.StoreRepository;
import com.sparta.baemineats.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;


    @Transactional
    public OrderResponse createOrder(OrderRequest request, User user){

        userRepository.findById(request.getUserId()).orElseThrow(()
                -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Store store = storeRepository.findById(request.getStoreId()).orElseThrow(()
                -> new IllegalArgumentException("음식점이 존재하지 않습니다."));

        Menu menu = menuRepository.findById(request.getMenuId()).orElseThrow(()
                -> new IllegalArgumentException("메뉴 존재하지 않습니다."));

        Order order = orderRepository.save(new Order(request, user, store, menu));

        return new OrderResponse(order);
    }

    @Transactional
    public List<OrderResponse> getOrders(){
        return orderRepository.findAll().stream().map(OrderResponse::new).toList();
              
    }

    @Transactional
    public void completeOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        order.setOrderState("주문완료");
        order.setOrderComplete(true);
        order.setConfirmTime(LocalDateTime.now());
        orderRepository.save(order);
    }

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