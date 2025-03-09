package com.sesac.boheommong.domain.user.service;

import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.entity.UserProduct;
import com.sesac.boheommong.domain.user.repository.UserProductRepository;
import com.sesac.boheommong.domain.user.repository.UserRepository;
import com.sesac.boheommong.global.exception.BaseException;
import com.sesac.boheommong.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProductService {

    private final UserProductRepository userProductRepository;
    private final UserRepository userRepository;

    /**
     * 특정 유저에게 상품을 구매(저장) 시킨다
     */
    public UserProduct createUserProduct(String userEmail, Long productId, Long paidAmount) {
        // userEmail을 통해 User 엔티티를 찾는다
        User user = userRepository.findByLoginEmail(userEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        // UserProduct 생성 후 저장
        UserProduct userProduct = UserProduct.builder()
                .user(user)
                .productId(productId)
                .paidAmount(paidAmount)
                .build();

        return userProductRepository.save(userProduct);
    }
    // UserProductService.java
    public List<UserProduct> getUserProducts(String userEmail) {
        User user = userRepository.findByLoginEmail(userEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        // 바뀐 메서드명
        return userProductRepository.findAllByUserUserId(user.getUserId());
    }

    public boolean isProductPurchased(String userEmail, Long productId) {
        User user = userRepository.findByLoginEmail(userEmail)
                .orElseThrow(() -> BaseException.from(ErrorCode.USER_NOT_FOUND));

        return userProductRepository.existsByUserUserIdAndProductId(user.getUserId(), productId);
    }

}
