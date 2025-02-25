package com.sesac.boheommong.domain.insurance.repository;

import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InsuranceProductRepository
        extends JpaRepository<InsuranceProduct, Long>, JpaSpecificationExecutor<InsuranceProduct>
{
}
