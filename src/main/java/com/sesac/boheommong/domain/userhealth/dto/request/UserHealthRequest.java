package com.sesac.boheommong.domain.userhealth.dto.request;

public record UserHealthRequest(
        Long userId,          // 어떤 유저의 건강정보인지
        String userName,
        Integer age,
        String gender,
        Float height,
        Float weight,
        Float bmi,
        Integer isSmoker,
        Integer isDrinker,
        Integer hasChronicDisease,
        String chronicDiseaseList,
        String surgeryHistory,
        String bloodPressure,
        String bloodSugar
) {}
