package com.sesac.boheommong.domain.recommendation.dto;

import com.sesac.boheommong.domain.recommendation.dto.RecommendedProductDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description="추천 이유 + 상품 목록")
public record RecommendationReasonGroupDto(
        @Schema(description="최종 문장 형태의 이유")
        String reason,

        @Schema(description="해당 카테고리 상품 최대6개")
        List<RecommendedProductDto> products
) {}
