package com.sesac.boheommong.domain.tosspayment.repository;

import com.sesac.boheommong.domain.tosspayment.entity.AutoPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoPaymentRepository extends JpaRepository<AutoPaymentEntity, Long> {

}

