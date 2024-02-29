package com.sparta.baemineats.entity;

import com.sparta.baemineats.dto.requestDto.ReviewRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reviews")
public class Review extends TimeStamped{
//..
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private double rate;


    public Review(ReviewRequest request, User user, Order order, Store store, Cart cart,Menu menu) {
        this.order = order;
        this.store = store;
        this.user = user;
        this.menu =menu;
        this.cart = cart;
        this.content = request.getContent();
        this.rate =request.getRate();
    }

    public void update(ReviewRequest request) {
        this.content = request.getContent();
        this.rate =request.getRate();

    }
}
