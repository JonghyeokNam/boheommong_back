package com.sesac.boheommong.domain.recommendation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="추천 상품 DTO")
public record RecommendedProductDto(
        @Schema(description="상품 ID", example="101")
        Long productId,

        @Schema(description="회사명", example="삼성화재")
        String companyName,

        @Schema(description="상품명", example="삼성화재 뉴 암플랜")
        String productName,

        @Schema(description="월 보험료", example="30000")
        Integer monthlyPremium,

        @Schema(description="보장내용", example="암,수술 등 보장")
        String coverageDetails
) {}
