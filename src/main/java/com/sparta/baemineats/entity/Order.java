package com.sparta.baemineats.entity;

import com.sparta.baemineats.dto.requestDto.OrderRequest;
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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;


    @Column(nullable = false)
    private boolean orderComplete;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column(nullable = true)
    private LocalDateTime confirmTime;

    @Column(nullable = false)
    private String orderState;


    public Order(OrderRequest request, User user, Store store, Menu menu) {
        this.store = store;
        this.user = user;
        this.menu = menu;
        this.orderComplete = true;
        this.createTime = LocalDateTime.now();
        this.orderState = request.getOrderState();
    }

    public void updateOrderState(OrderUpdate orderUpdate){
        this.orderState = orderUpdate.getOrderState();
    }
}
