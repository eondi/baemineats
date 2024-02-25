package com.sparta.baemineats.entitiy;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "menus")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false, unique = true)
    private String menuName;

    @Column(nullable = false)
    private int menuPrice;

    @Column(nullable = false)
    private String menuDescription;

}
