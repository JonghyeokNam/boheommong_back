package com.sesac.boheommong.domain.tosspayment.service;

import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import com.sesac.boheommong.domain.insurance.repository.InsuranceProductRepository;
import com.sesac.boheommong.domain.tosspayment.dto.AutoPaymentDto;
import com.sesac.boheommong.domain.tosspayment.entity.AutoPayment;
import com.sesac.boheommong.domain.tosspayment.repository.AutoPaymentRepository;
import com.sesac.boheommong.domain.user.entity.User;
import com.sesac.boheommong.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AutoPaymentService {

    private final AutoPaymentRepository autoPaymentRepository;
    private final UserRepository userRepository;
    private final InsuranceProductRepository productRepository;

    /**
     * 자동결제 생성
     * - 엔티티의 user, product는 updatable=false → 생성 시점에만 설정 가능
     */
    public AutoPaymentDto createAutoPayment(AutoPaymentDto dto) {
        // 1) 유저 조회
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Invalid userId=" + dto.getUserId()));

        // 2) 상품 조회
        InsuranceProduct product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Invalid productId=" + dto.getProductId()));

        // 3) AutoPayment 엔티티 생성 (정적 팩토리 사용)
        AutoPayment autoPayment = AutoPayment.create(
                user,
                product,
                dto.getDayOfMonth(),
                dto.getTime()
        );

        // 4) 저장
        AutoPayment saved = autoPaymentRepository.save(autoPayment);

        // 5) 결과 변환
        return AutoPaymentDto.fromEntity(saved);
    }

    /**
     * 전체 자동결제 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AutoPaymentDto> getAllAutoPayments() {
        return autoPaymentRepository.findAll().stream()
                .map(AutoPaymentDto::fromEntity)
                .toList();
    }

    /**
     * 특정 자동결제 단건 조회
     */
    @Transactional(readOnly = true)
    public AutoPaymentDto getAutoPaymentDetail(Long autoPaymentId) {
        AutoPayment entity = autoPaymentRepository.findById(autoPaymentId)
                .orElseThrow(() -> new RuntimeException("AutoPayment not found. id=" + autoPaymentId));
        return AutoPaymentDto.fromEntity(entity);
    }

    /**
     * 특정 사용자의 자동결제 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AutoPaymentDto> getAutoPaymentsByUser(Long userId) {
        return autoPaymentRepository.findByUser_UserId(userId).stream()
                .map(AutoPaymentDto::fromEntity)
                .toList();
    }

    /**
     * 자동결제 수정 (날짜, 시간만)
     * - user, product는 updatable=false이므로 변경 불가
     */
    public AutoPaymentDto updateAutoPayment(Long autoPaymentId, AutoPaymentDto dto) {
        AutoPayment autoPayment = autoPaymentRepository.findById(autoPaymentId)
                .orElseThrow(() -> new RuntimeException("AutoPayment not found. id=" + autoPaymentId));

        // 수정 가능한 필드만 업데이트
        autoPayment.updateDayOfMonth(dto.getDayOfMonth());
        autoPayment.updateTime(dto.getTime());

        // flush at commit
        return AutoPaymentDto.fromEntity(autoPayment);
    }

    /**
     * 자동결제 삭제
     */
    public void deleteAutoPayment(Long autoPaymentId) {
        AutoPayment autoPayment = autoPaymentRepository.findById(autoPaymentId)
                .orElseThrow(() -> new RuntimeException("AutoPayment not found. id=" + autoPaymentId));
        autoPaymentRepository.delete(autoPayment);
    }
}
