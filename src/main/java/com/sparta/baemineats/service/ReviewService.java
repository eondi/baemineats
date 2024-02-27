package com.sparta.baemineats.service;

import com.sparta.baemineats.dto.requestDto.ReviewRequest;
import com.sparta.baemineats.dto.responseDto.ReviewResponse;
import com.sparta.baemineats.entity.*;
import com.sparta.baemineats.repository.MenuRepository;
import com.sparta.baemineats.repository.OrderRepository;
import com.sparta.baemineats.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreService storeService;
    private final MenuRepository menuRepository;
    public ReviewResponse createReview(Long orderId, ReviewRequest request, User user) {
        // 권한 판단
        if (!user.getRole().equals(UserRoleEnum.USER) )
            throw new IllegalArgumentException("일반 유저만 리뷰 가능합니다.");

        // 주문 조회
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 주문이 없습니다.")
        );

        //매장 조회
        Store store = storeService.findStore(order.getStore().getStoreId());

        //메뉴 조회
        Menu menu = menuRepository.findById(orderId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 메뉴가 없습니다.")
        );

        //리뷰 생성
        Review review = reviewRepository.save(new Review(request, user, order, store, menu));

        return new ReviewResponse(review);

    }

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

    public String deleteStore(Long reviewId, User user) {

        // 해당 리뷰 조회
        Review review = findReview(reviewId);
        String menuName = review.getMenu().getMenuName();

        // 리뷰 유저 확인 - 일반유저
        if (!review.getUser().getUsername().equals(user.getUsername()) && user.getRole().equals(UserRoleEnum.USER))
            throw new IllegalArgumentException("다른 사용자의 리뷰 삭제는 불가능합니다.");

        // 리뷰 유저 확인 - 판매자
        if (!review.getStore().getUser().getUsername().equals(user.getUsername()) && user.getRole().equals(UserRoleEnum.SELLER))
            throw new IllegalArgumentException("다른 매장의 리뷰 삭제는 불가능합니다.");

        // 리뷰 수정
        reviewRepository.delete(review);

        return  menuName;
    }

//    public List<Review> getStoreReview(Long orderId, User user) {
//    }
}
