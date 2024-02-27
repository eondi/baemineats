package com.sparta.baemineats.entity;

import com.sparta.baemineats.dto.requestDto.CartRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

//    @ManyToOne
//    @JoinColumn(name = "menu_id")
//    private Menu menu;

    @OneToMany
    private List<Menu> menus = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private int totalPrice;

    public Cart(CartRequest request, User user, Store store, List<Menu> menus) {
        this.user = user;
        this.store = store;
        this.menus = menus;
        this.quantity = request.getQuantity();
        this.totalPrice = request.getTotalPrice();
    }
}
