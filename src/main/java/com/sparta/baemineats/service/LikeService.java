package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.responseDto.LikeStoreResponse;
import com.sparta.baemineats.dto.responseDto.LikeUserResponse;
import com.sparta.baemineats.entity.Like;
import com.sparta.baemineats.entity.Store;
import com.sparta.baemineats.entity.User;
import com.sparta.baemineats.entity.UserRoleEnum;
import com.sparta.baemineats.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final StoreService storeService;
    private final LikeRepository likeRepository;


    public String likeStore(Long storeId, User user) {
        // 권한 판단
        checkRole(user);

        // 해당 매장 조회
        Store store = storeService.findStore(storeId);
        String storeName = store.getStoreName();

        // 좋아요 확인
        List<Like> likeList = likeRepository.findByUser_UserId(user.getUserId());

        for (Like like : likeList){
            if (like.getStore().getStoreId().equals(storeId)){
                throw new IllegalArgumentException("좋아요는 중복이 불가합니다");
                }
        }

        likeRepository.save(new Like(store, user));

        return  storeName;
    }

    public String unlikeStore(Long storeId, User user) {
        checkRole(user);
        List<Like> likeList = likeRepository.findByUser_UserId(user.getUserId());
        for (Like like : likeList){
            if (like.getStore().getStoreId().equals(storeId)){
                String storeName = like.getStore().getStoreName();
                likeRepository.delete(like);
                return storeName;
            }
        }
        throw new IllegalArgumentException("해당 음식점에 좋아요를 누른적이 없습니다.");

    }

    private void checkRole(User user) {
        if (!user.getRole().equals(UserRoleEnum.USER) )
            throw new IllegalArgumentException("일반 사용자만 가능합니다");
    }


    public List<LikeUserResponse> getUserlike(Long storeId, User user) {
        if (!user.getRole().equals(UserRoleEnum.SELLER) )
            throw new IllegalArgumentException("판매자만 조회가 가능합니다");

        List<Like> likeList = likeRepository.findALLByStore_StoreId(storeId);
        List<LikeUserResponse> likeUserResponseList = new ArrayList<>();

        for (Like like : likeList) {
            likeUserResponseList.add(new LikeUserResponse(like.getUser().getUsername()));
        }
        return likeUserResponseList;
    }

    public List<LikeStoreResponse> getStorelike(User user) {
        checkRole(user);

        List<Like> likeList = likeRepository.findALLByUser_UserId(user.getUserId());
        List<LikeStoreResponse> likeUserResponseList = new ArrayList<>();

        for (Like like : likeList) {
            likeUserResponseList.add(new LikeStoreResponse(like.getStore().getStoreName()));
        }
        return likeUserResponseList;

    }

    public int getlikeCount(Long storeId, User user) {
        List<Like> likeList = likeRepository.findALLByStore_StoreId(storeId);

        return likeList.size();

    }
}
