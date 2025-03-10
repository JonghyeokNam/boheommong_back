package com.sesac.boheommong.domain.tosspayment.repository;

import com.sesac.boheommong.domain.tosspayment.entity.AutoPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoPaymentRepository extends JpaRepository<AutoPayment, Long> {
    /**
     * 특정 사용자의 자동결제 목록 조회
     * AutoPayment 엔티티 내부의 user 필드(연관관계) → user.userId
     */
    List<AutoPayment> findByUser_UserId(Long userId);

    List<AutoPayment> findByDayOfMonth(int dayOfMonth);
}
