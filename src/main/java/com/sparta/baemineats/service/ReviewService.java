package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.ReviewRequest;
import com.sparta.baemineats.dto.responseDto.ReviewResponse;
import com.sparta.baemineats.dto.responseDto.StoreReviewResponse;
import com.sparta.baemineats.entity.*;
import com.sparta.baemineats.repository.CartRepository;
import com.sparta.baemineats.repository.MenuRepository;
import com.sparta.baemineats.repository.OrderRepository;
import com.sparta.baemineats.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreService storeService;
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;

    public ReviewResponse createReview(ReviewRequest request, User user) {
        // 권한 판단
        if (!user.getRole().equals(UserRoleEnum.USER))
            throw new IllegalArgumentException("일반 유저만 리뷰 가능합니다.");

        // 주문 조회
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 주문이 없습니다.")
        );

        // 장바구니 조회
        Cart cart = cartRepository.findById(request.getCartId()).orElseThrow(
                () -> new IllegalArgumentException("주문건만 리뷰 가능합니다")
        );

        // 매장 확인
        Store store = storeService.findStore(request.getStoreId());

        // 메뉴 조회
        Menu menu = menuRepository.findById(request.getMenuId()).orElseThrow(
                () -> new IllegalArgumentException("주문한 메뉴만 리뷰 가능합니다")
        );

        //리뷰 생성
        Review review = reviewRepository.save(new Review(request, user, order, store, cart,menu));

        return new ReviewResponse(review);

    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewRequest request, User user) {
        // 권한 판단
        if (!user.getRole().equals(UserRoleEnum.USER) )
            throw new IllegalArgumentException("일반 사용자 수정이 가능합니다.");


        // 해당 리뷰 조회
        Review review = findReview(reviewId);

        // 리뷰 유저 확인
        if (!review.getUser().getUsername().equals(user.getUsername()))
            throw new IllegalArgumentException("다른 사용자의 리뷰 수정은 불가능합니다.");

        // 리뷰 수정
        review.update(request);

        return new ReviewResponse(review);

    }

    public Review findReview(Long reviewId){

        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 리뷰가 없습니다.")
        );

        return review;

    }

    @Transactional
    public String deleteStore(Long reviewId, User user) {

        // 해당 리뷰 조회
        Review review = findReview(reviewId);
        String menuName = review.getMenu().getMenuName();

        // 리뷰 유저 확인 - 일반유저
        if (!review.getUser().getUsername().equals(user.getUsername()) && user.getRole().equals(UserRoleEnum.USER))
            throw new IllegalArgumentException("다른 사용자의 리뷰 삭제는 불가능합니다.");

        // 리뷰 수정
        reviewRepository.delete(review);

        return  menuName;
    }

    public List<StoreReviewResponse> getStoreReview(Long storeId, User user) {
        List<Review> reviewList = reviewRepository.findByStore_StoreId(storeId);

        List<StoreReviewResponse> reviewResponseList = new ArrayList<>();

        // 매장 이름
        for (Review review : reviewList) {
            reviewResponseList.add(new StoreReviewResponse(review));
        }
        return  reviewResponseList;
    }
}
