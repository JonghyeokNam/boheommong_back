package com.sesac.boheommong.domain.userhealth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 사용자 건강정보 등록/수정 시 사용하는 요청 DTO
 */
@Schema(description = "사용자 건강정보 요청 DTO")
public record UserHealthRequestDto(
        @Schema(description = "나이", example = "30")
        Integer age,

        @Schema(description = "성별 (M/F)", example = "M")
        String gender,

        @Schema(description = "키(cm)", example = "170")
        Integer height,

        @Schema(description = "몸무게(kg)", example = "65")
        Integer weight,

        @Schema(description = "혈압 레벨(1~5)", example = "2")
        Integer bloodPressureLevel,

        @Schema(description = "혈당 레벨(1~5)", example = "2")
        Integer bloodSugarLevel,

        @Schema(description = "수술 횟수(0~5)", example = "0")
        Integer surgeryCount,

        @Schema(description = "흡연 여부", example = "false")
        Boolean isSmoker,

        @Schema(description = "음주 여부", example = "true")
        Boolean isDrinker,

        @Schema(
                description = "만성질환 목록(배열)",
                example = "[\"고혈압\", \"당뇨\"]"
        )
        List<String> chronicDiseaseList,

        @Schema(description = "직업(OFFICE/DELIVERY/CONSTRUCTION...)", example = "OFFICE")
        String jobType,

        @Schema(description = "자녀 유무", example = "false")
        Boolean hasChildren,

        @Schema(description = "자가주택 여부", example = "true")
        Boolean hasOwnHouse,

        @Schema(description = "반려동물 여부", example = "false")
        Boolean hasPet,

        @Schema(description = "가족력 여부", example = "false")
        Boolean hasFamilyHistory
) {}
