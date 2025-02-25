package com.sesac.boheommong.domain.insurance.service;

import com.sesac.boheommong.domain.insurance.dto.response.InsuranceProductResponseDto;
import com.sesac.boheommong.domain.insurance.enums.InsuranceType;
import com.sesac.boheommong.domain.insurance.repository.InsuranceProductRepository;
import com.sesac.boheommong.domain.insurance.spec.InsuranceProductSpec;
import com.sesac.boheommong.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InsuranceProductServiceImpl implements InsuranceProductService {

    private final InsuranceProductRepository productRepo;
    private final UserRepository userRepo;

    @Override
    public Page<InsuranceProductResponseDto> searchProducts(
            int page,
            int size,
            List<String> companyNames,
            List<InsuranceType> categories,
            String productName
    ) {
        var pageable = PageRequest.of(page, size);

        // Specification 만들기 (companyNames, categories, productName)
        var spec = InsuranceProductSpec.withFilter(companyNames, categories, productName);

        // findAll(spec, pageable)
        var entityPage = productRepo.findAll(spec, pageable);

        // map -> DTO
        return entityPage.map(InsuranceProductResponseDto::fromEntity);
    }
}
