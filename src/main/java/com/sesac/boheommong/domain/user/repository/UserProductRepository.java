package com.sesac.boheommong.domain.user.repository;

import com.sesac.boheommong.domain.user.entity.UserProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// UserProductRepository.java
public interface UserProductRepository extends JpaRepository<UserProduct, Long> {

    // BEFORE: List<UserProduct> findAllByUserId(Long userId);
    // AFTER:  List<UserProduct> findAllByUserUserId(Long userId);
    List<UserProduct> findAllByUserUserId(Long userId);

    boolean existsByUserUserIdAndProductId(Long userId, Long productId);
}

