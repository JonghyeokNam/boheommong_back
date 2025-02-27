package com.sesac.boheommong.domain.insurance.service;

import com.sesac.boheommong.domain.insurance.dto.response.InsuranceProductResponseDto;
import com.sesac.boheommong.domain.insurance.enums.InsuranceType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InsuranceProductService {

    /**
     * [페이징 + 다중 필터(회사명/카테고리) + 상품명 검색]
     */
    Page<InsuranceProductResponseDto> searchProducts(
            int page,
            int size,
            List<String> companyNames,
            List<InsuranceType> categories,
            String productName
    );
}
