package com.sesac.boheommong.domain.insurance.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "보험상품 가입 요청 DTO")
public record InsuranceJoinRequestDto(
        @Schema(description="가입자 이름", example="새싹이")
        String joinerName,

        @Schema(description="특약/추가옵션", example="수술비 특약 추가")
        String extraOptions
) {}
