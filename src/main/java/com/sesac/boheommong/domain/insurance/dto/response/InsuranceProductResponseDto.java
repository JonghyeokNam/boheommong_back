package com.sesac.boheommong.domain.insurance.dto.response;

import com.sesac.boheommong.domain.insurance.entity.InsuranceProduct;
import com.sesac.boheommong.domain.insurance.enums.InsuranceType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "보험상품 조회 응답 DTO (페이징/필터)")
public record InsuranceProductResponseDto(
        @Schema(description="상품 ID", example="1")
        Long productId,

        @Schema(description="회사명", example="삼성화재")
        String companyName,

        @Schema(description="상품명", example="삼성화재 뉴 암플랜")
        String productName,

        @Schema(description="월 보험료", example="30000")
        Integer monthlyPremium,

        @Schema(description="카테고리", example="CANCER, SURGERY 등")
        InsuranceType productCategory,

        @Schema(description="보장 내용", example="암, 수술비 등...")
        String coverageDetails
) {
    public static InsuranceProductResponseDto fromEntity(InsuranceProduct e) {
        if (e == null) return null;
        return new InsuranceProductResponseDto(
                e.getProductId(),
                e.getCompanyName(),
                e.getProductName(),
                e.getMonthlyPremium(),
                e.getProductCategory(),
                e.getCoverageDetails()
        );
    }
}
