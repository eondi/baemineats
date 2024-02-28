package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.OrderUpdate;
import com.sparta.baemineats.dto.responseDto.OrderResponse;
import com.sparta.baemineats.entity.*;
import com.sparta.baemineats.repository.CartRepository;
import com.sparta.baemineats.repository.OrderRepository;
import com.sparta.baemineats.repository.StoreRepository;
import com.sparta.baemineats.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final CartRepository cartRepository;
//    private final MenuRepository menuRepository;
//    private final UserRepository userRepository;

//    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);



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

//    public List<OrderResponse> getOrders(UserDetailsImpl userDetails){
//        User user = userDetails.getUser();
//        String sellerName = user.getUsername();
//        Store store = storeRepository.findBySellerName(sellerName);
//        logger.info("Store: " + store);  // 로그 출력
//        logger.info("Username: " + user.getUsername());  // 로그 출력
//        // ...
//        return null;
//    }
    @Transactional
    public List<OrderResponse> getOrders(UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        if (user.getRole().equals(UserRoleEnum.USER)) {
            // 사용자가 주문한 주문만 조회
            return orderRepository.findByUser(user).stream().map(OrderResponse::new).toList();
        } else if (user.getRole().equals(UserRoleEnum.SELLER)) {
            // 판매자가 운영하는 가게에 대한 주문만 조회
            String sellerName = user.getUsername();
            Store store = storeRepository.findBySellerName(sellerName);
            return orderRepository.findByStore(store).stream().map(OrderResponse::new).toList();
        }
        // todo: 주문조회기능 따로 만들기
        throw new IllegalArgumentException("주문 조회 권한이 없습니다.");
    }

    @Transactional
    public void updateOrderState(Long orderId, OrderUpdate orderUpdate, User user){
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        if (!order.getUser().equals(user)) {
            throw new IllegalArgumentException("주문자만 주문 상태를 변경할 수 있습니다.");
        }
        order.updateOrderState(orderUpdate);
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long orderId, User user){
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