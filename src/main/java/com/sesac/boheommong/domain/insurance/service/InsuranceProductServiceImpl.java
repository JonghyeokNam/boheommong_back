package com.sesac.boheommong.domain.insurance.service;

import com.sesac.boheommong.domain.insurance.dto.response.InsuranceProductResponseDto;
import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import com.sesac.boheommong.domain.insurance.enums.InsuranceType;
import com.sesac.boheommong.domain.insurance.repository.InsuranceProductRepository;
import com.sesac.boheommong.domain.insurance.spec.InsuranceProductSpec;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InsuranceProductServiceImpl implements InsuranceProductService {

    private final InsuranceProductRepository productRepo;

    @Override
    public Page<InsuranceProductResponseDto> searchProducts(
            int page,   // 이미 0-based 로 변환됨
            int size,
            List<String> companyNames,
            List<InsuranceType> categories,
            String productName
    ) {
        // 1) PageRequest (0-based page)
        Pageable pageable = PageRequest.of(page, size);

        // 2) 필터링 조건 (정확 매칭 - company, category)
        Specification<InsuranceProduct> spec = InsuranceProductSpec.withFilter(
                companyNames, categories, productName
        );

        // 3) findAll + map -> DTO
        return productRepo.findAll(spec, pageable)
                .map(InsuranceProductResponseDto::fromEntity);
    }
}
