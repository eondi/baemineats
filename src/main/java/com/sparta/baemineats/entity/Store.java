package com.sparta.baemineats.entity;

import com.sparta.baemineats.dto.requestDto.StoreRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "stores")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable = false, unique = true)
    private String storeName;

    @Column(nullable = false, unique = true)
    private String storeDescription;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Store(StoreRequest request, User user) {
        this.storeName = request.getStoreName();
        this.storeDescription = request.getStoreDescription();
        this.user = user;
    }

    public void update(StoreRequest request) {
        this.storeName = request.getStoreName();
        this.storeDescription = request.getStoreDescription();
    }
}
