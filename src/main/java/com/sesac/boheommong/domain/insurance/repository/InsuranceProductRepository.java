package com.sesac.boheommong.domain.insurance.repository;

import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceProductRepository extends JpaRepository<InsuranceProduct, Long> {
}
