package com.sesac.boheommong.domain.insurance.repository;

import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InsuranceProductRepository extends JpaRepository<InsuranceProduct, Long> {
    Optional<InsuranceProduct> findInsuranceProductByProductId(Long productId);
}
