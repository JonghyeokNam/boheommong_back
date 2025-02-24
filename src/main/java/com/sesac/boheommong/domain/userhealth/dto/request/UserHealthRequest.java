package com.sesac.boheommong.domain.userhealth.dto.request;

public record UserHealthRequest(
        Long userId,
        Integer age,
        String gender,            // "M", "F"
        Integer height,           // cm
        Integer weight,           // kg
        Integer bloodPressureLevel,  // 1~5
        Integer bloodSugarLevel,     // 1~5
        Integer surgeryCount,        // 0~5
        Boolean isSmoker,
        Boolean isDrinker,
        String chronicDiseaseList,   // "고혈압,당뇨"
        String jobType,              // "OFFICE", "DELIVERY", etc.
        Boolean hasChildren,
        Boolean hasOwnHouse,
        Boolean hasPet,
        Boolean hasFamilyHistory
) {}
