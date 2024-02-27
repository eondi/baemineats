package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.CartRequest;
import com.sparta.baemineats.dto.responseDto.CartResponse;
import com.sparta.baemineats.entity.Cart;
import com.sparta.baemineats.entity.Menu;
import com.sparta.baemineats.entity.Store;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.repository.CartRepository;
import com.sparta.baemineats.repository.MenuRepository;
import com.sparta.baemineats.repository.StoreRepository;
import com.sparta.baemineats.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    public CartResponse addToCart(CartRequest request, User user) {
//        userRepository.findById(user.getUserId()).orElseThrow(()
//                -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Store store = storeRepository.findById(request.getStoreId()).orElseThrow(()
                -> new IllegalArgumentException("음식점이 존재하지 않습니다."));

        Menu menu = menuRepository.findById(request.getMenuId()).orElseThrow(()
                -> new IllegalArgumentException("메뉴 존재하지 않습니다."));

        Cart cart = cartRepository.save(new Cart(request, user, store, menu));

        return new CartResponse(cart);
    }

    public List<CartResponse> getCarts() {
        return cartRepository.findAll().stream().map(CartResponse::new).toList();

    }

    public void deleteCart(Long menuId, User user) {
        Cart cart = findOne(menuId);

        if (!cart.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("주문자만 취소할 수 있습니다.");
        }
        cartRepository.delete(cart);
    }

    private Cart findOne(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(()
                -> new IllegalArgumentException("주문이 존재하지 않습니다."));
    }
}
