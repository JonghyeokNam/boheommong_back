package com.sesac.boheommong.domain.recommendation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description="전체 추천 결과 DTO")
public record RecommendationResponseDto(

        @Schema(description="최상단 인트로 문구(전체 위험 요소)")
        String globalIntro,

        @Schema(description="카테고리별 추천 목록")
        List<RecommendationReasonGroupDto> categories

) {}
