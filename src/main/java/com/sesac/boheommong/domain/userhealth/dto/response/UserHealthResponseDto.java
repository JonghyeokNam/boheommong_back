package com.sesac.boheommong.domain.userhealth.dto.response;

import com.sesac.boheommong.domain.user.dto.response.UserResponseDto;
import com.sesac.boheommong.domain.userhealth.entity.UserHealth;
import com.sesac.boheommong.domain.userhealth.enums.JobType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 건강정보(UserHealth)를 내려줄 때 사용할 응답 DTO
 * record 형식
 */
@Schema(description = "사용자 건강정보 응답 DTO")
public record UserHealthResponseDto(
        @Schema(description = "건강정보 ID", example = "10")
        Long healthId,

        @Schema(description = "연결된 사용자 정보")
        UserResponseDto user,

        @Schema(description = "나이", example = "30")
        Integer age,

        @Schema(description = "성별 (M/F)", example = "M")
        String gender,

        @Schema(description = "키(cm)", example = "170")
        Integer height,

        @Schema(description = "몸무게(kg)", example = "65")
        Integer weight,

        @Schema(description = "BMI", example = "22.49")
        Float bmi,

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

        @Schema(description = "만성질환", example = "[\"고혈압\",\"당뇨\"]")
        List<String> chronicDiseaseList,

        @Schema(description = "직업", example = "OFFICE/DELIVERY/CONSTRUCTION 등")
        JobType jobType,

        @Schema(description = "자녀 유무", example = "false")
        Boolean hasChildren,

        @Schema(description = "자가주택 여부", example = "true")
        Boolean hasOwnHouse,

        @Schema(description = "반려동물 여부", example = "false")
        Boolean hasPet,

        @Schema(description = "가족력 여부", example = "false")
        Boolean hasFamilyHistory
) {

    /**
     * [fromEntity]
     * UserHealth -> UserHealthResponseDto 변환
     */
    public static UserHealthResponseDto fromEntity(UserHealth uh) {
        if (uh == null) return null;

        List<String> diseaseList = new ArrayList<>();
        if (uh.getChronicDiseaseList() != null && !uh.getChronicDiseaseList().isEmpty()) {
            diseaseList = Arrays.stream(uh.getChronicDiseaseList().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        return new UserHealthResponseDto(
                uh.getHealthId(),
                UserResponseDto.toDto(uh.getUser()),
                uh.getAge(),
                uh.getGender(),
                uh.getHeight(),
                uh.getWeight(),
                uh.getBmi(),
                uh.getBloodPressureLevel(),
                uh.getBloodSugarLevel(),
                uh.getSurgeryCount(),
                uh.getIsSmoker(),
                uh.getIsDrinker(),
                diseaseList,
                uh.getJobType(),
                uh.getHasChildren(),
                uh.getHasOwnHouse(),
                uh.getHasPet(),
                uh.getHasFamilyHistory()
        );
    }
}
