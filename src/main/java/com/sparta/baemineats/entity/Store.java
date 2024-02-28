package com.sparta.baemineats.entity;

import com.sparta.baemineats.dto.requestDto.StoreRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "stores")
public class Store extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable = false, unique = true)
    private String storeName;

    @Column(nullable = false)
    private String storeDescription;

    @Column(nullable = false)
    private String sellerName;


    public Store(StoreRequest request, String username) {
        this.storeName = request.getStoreName();
        this.storeDescription = request.getStoreDescription();
        this.sellerName = username;
    }

    public void update(StoreRequest request) {
        this.storeName = request.getStoreName();
        this.storeDescription = request.getStoreDescription();
    }

    @Builder
    public Store(Long id, String storeName, String storeDescription, String username) {
        this.storeId = id;
        this.storeName = storeName;
        this.storeDescription = storeDescription;
        this.sellerName = username;
    }
}
