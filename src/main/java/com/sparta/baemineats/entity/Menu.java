package com.sparta.baemineats.entity;

import com.sparta.baemineats.dto.requestDto.MenuRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "menus")
@NoArgsConstructor
public class Menu extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false, unique = true)
    private String menuName;

    @Column(nullable = false)
    private int menuPrice;

    @Column(nullable = false)
    private String menuDescription;

    public Menu(MenuRequest requestDto, Store store) {
        this.menuName = requestDto.getMenuName();
        this.menuPrice = requestDto.getMenuPrice();
        this.menuDescription = requestDto.getMenuDescription();
        this.store = store;
    }

    public void update(MenuRequest requestDto) {
        this.menuName = requestDto.getMenuName();
        this.menuPrice = requestDto.getMenuPrice();
        this.menuDescription = requestDto.getMenuDescription();
    }
}
