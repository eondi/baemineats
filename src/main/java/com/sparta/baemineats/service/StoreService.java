package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.StoreRequest;
import com.sparta.baemineats.dto.responseDto.StoreResponse;
import com.sparta.baemineats.entity.Like;
import com.sparta.baemineats.entity.Store;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.entity.UserRoleEnum;
import com.sparta.baemineats.repository.LikeRepository;
import com.sparta.baemineats.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final LikeRepository likeRepository;

    public StoreResponse createStore(StoreRequest request, User user) {
        // 권한 판단
        if (user.getRole().equals(UserRoleEnum.USER) )
            throw new IllegalArgumentException("관리자, 판매자만 등록이 가능합니다.");

        // 매장 등록
        Store store = storeRepository.save(new Store(request, user));

        return new StoreResponse(store);
    }

    public List<StoreResponse> getStoreNames(User user) {
        // 매장 전체 조회
        List<Store> storeList = storeRepository.findAll();
        List<StoreResponse> storeResponsesList = new ArrayList<>();

        // 매장 이름 
        for (Store store : storeList) {
            storeResponsesList.add(new StoreResponse(store.getStoreName()));
        }

        return  storeResponsesList;

    }


    public StoreResponse getStore(Long storeId, User user) {

        // 해당 매장 조회
        Store store = findStore(storeId);
        return new StoreResponse(store);

    }

    public StoreResponse updateStore(Long storeId, StoreRequest request, User user) {

        // 권한 판단
        if (!user.getRole().equals(UserRoleEnum.SELLER) )
            throw new IllegalArgumentException("판매자만 수정이 가능합니다.");


        // 해당 매장 조회
        Store store = findStore(storeId);

        // 매장 유저 확인
        if (!store.getUser().getUsername().equals(user.getUsername()))
            throw new IllegalArgumentException("다른 판매자의 매장 수정은 불가능합니다.");

        // 매장 수정
        store.update(request);

        return new StoreResponse(store);

    }

    public Store findStore(Long storeId){

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 음식점이 없습니다.")
        );

        return store;

    }

    public String deleteStore(Long storeId, User user) {

        // 권한 판단
        if (user.getRole().equals(UserRoleEnum.USER) )
            throw new IllegalArgumentException("관리자, 판매자만 삭제 가능합니다.");


        // 해당 매장 조회
        Store store = findStore(storeId);
        String storeName = store.getStoreName();

        // 매장 유저 확인
        if (!store.getUser().getUsername().equals(user.getUsername()) && user.getRole().equals(UserRoleEnum.SELLER))
            throw new IllegalArgumentException("판매자의 경우 다른 판매자의 매장 수정은 불가능합니다.");

        // 매장 삭제
        storeRepository.delete(store);

        return storeName;
    }

    public String likeStore(Long storeId, User user) {
        // 권한 판단
        if (!user.getRole().equals(UserRoleEnum.SELLER) )
            throw new IllegalArgumentException("일반 사용자만 좋아요가 가능합니다");

        // 해당 매장 조회
        Store store = findStore(storeId);
        String storeName = store.getStoreName();

        // 좋아요 확인
        if (likeRepository.findByUser_UserId(user.getUserId()).isPresent() && likeRepository.findByStore_StoreId(store.getStoreId()).isPresent()) {
            throw new IllegalArgumentException("좋아요는 중복이 불가합니다");
        }
        else{
            Like like = likeRepository.save(new Like(store, user));
        }

        return  storeName;
    }
}
