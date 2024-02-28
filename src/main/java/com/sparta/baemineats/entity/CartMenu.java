package com.sparta.baemineats.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cartmenu")
public class CartMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartMenuId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

}
