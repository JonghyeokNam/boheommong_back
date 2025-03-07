package com.sesac.boheommong.domain.tosspayment.service;

import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import com.sesac.boheommong.domain.insurance.repository.InsuranceProductRepository;
import com.sesac.boheommong.domain.tosspayment.dto.AutoPaymentDto;
import com.sesac.boheommong.domain.tosspayment.entity.AutoPaymentEntity;
import com.sesac.boheommong.domain.tosspayment.repository.AutoPaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutoPaymentService {

    private final AutoPaymentRepository repository;
    private final InsuranceProductRepository productRepository;

    public AutoPaymentService(AutoPaymentRepository repository,
                              InsuranceProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    /**
     * 자동이체 생성
     *  - productId가 포함된 경우, 해당 보험상품과 연관관계 설정
     */
    public AutoPaymentDto createAutoPayment(AutoPaymentDto dto) {
        AutoPaymentEntity entity = new AutoPaymentEntity();
        entity.setUserId(dto.getUserId());
        entity.setDayOfMonth(dto.getDayOfMonth());
        entity.setTime(dto.getTime());

        if (dto.getProductId() != null) {
            InsuranceProduct product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Invalid productId: " + dto.getProductId()));
            entity.setInsuranceProduct(product);
        }

        AutoPaymentEntity saved = repository.save(entity);
        return AutoPaymentDto.fromEntity(saved);
    }

    /**
     * 전체 자동이체 목록 조회
     */
    public List<AutoPaymentDto> getAllAutoPayments() {
        List<AutoPaymentEntity> entities = repository.findAll();
        return entities.stream()
                .map(AutoPaymentDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 단건 상세조회
     */
    public AutoPaymentDto getAutoPaymentDetail(Long id) {
        AutoPaymentEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("AutoPayment not found with id=" + id));
        return AutoPaymentDto.fromEntity(entity);
    }

    /**
     * 자동이체 단건 업데이트
     *  - productId가 있으면 해당 상품을 찾아서 연관관계 설정
     *  - productId가 null이면 연관관계 해제
     */
    public AutoPaymentDto updateAutoPayment(Long id, AutoPaymentDto dto) {
        AutoPaymentEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("AutoPayment not found with id=" + id));

        entity.setDayOfMonth(dto.getDayOfMonth());
        entity.setTime(dto.getTime());

        if (dto.getProductId() != null) {
            InsuranceProduct product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Invalid productId: " + dto.getProductId()));
            entity.setInsuranceProduct(product);
        } else {
            entity.setInsuranceProduct(null);
        }

        AutoPaymentEntity saved = repository.save(entity);
        return AutoPaymentDto.fromEntity(saved);
    }

    /**
     * 자동이체 삭제
     */
    public void deleteAutoPayment(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("AutoPayment not found with id=" + id));
        repository.deleteById(id);
    }
}
