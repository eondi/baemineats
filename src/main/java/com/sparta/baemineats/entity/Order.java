package com.sparta.baemineats.entity;

import com.sparta.baemineats.dto.requestDto.OrderUpdate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;


    @Column(nullable = false)
    private boolean orderComplete;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column(nullable = true)
    private LocalDateTime confirmTime;

    @Column(nullable = false)
    private OrderStateEnum orderState;


    public Order(User user, Store store, Menu menu) {
        this.store = store;
        this.user = user;
        this.menu = menu;
        this.orderComplete = false;
        this.createTime = LocalDateTime.now();
        // todo : 후에 기능추가
        this.orderState = OrderStateEnum.ORDERED; // 초기 상태 설정
    }

    public void updateOrderState(OrderUpdate orderUpdate){
        this.orderState = orderUpdate.getOrderState();
    }

    public enum OrderStateEnum {
        ORDERED,     // 주문 완료
        PREPARING,   // 준비 중
        DELIVERING,  // 배송 중
        DELIVERED,   // 배송 완료
        CANCELED     // 주문 취소
    }
}
