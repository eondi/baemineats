package com.sparta.baemineats.entity;

import com.sparta.baemineats.dto.requestDto.CartRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private int totalPrice;

    public Cart(CartRequest request, User user, Store store, Menu menu) {
        this.user = user;
        this.menu = menu;
        this.store = store;
        this.quantity = request.getQuantity();
        this.totalPrice = request.getTotalPrice();
    }
}
