package com.sesac.boheommong.domain.user.dto.request;

public record UserInfoRequestDto(
        // 유저가 추가로 입력할 일반 이메일
        String userEmail,

        // 나이(age)
        Integer age,

        // 성별(gender)
        String gender,

        // 키(height)
        Float height,

        // 몸무게(weight)
        Float weight,

        // 흡연 여부
        Boolean isSmoker,

        // 음주 여부
        Boolean isDrinker,

        // 만성질환 여부
        Boolean hasChronicDisease,

        // 만성질환 리스트
        String chronicDiseaseList,

        // 수술 이력
        String surgeryHistory,

        // 마지막 건강검진 날짜
        String lastCheckupDate,

        // 혈압
        String bloodPressure,

        // 혈당
        String bloodSugar
) {
}
