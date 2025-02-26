package com.sesac.boheommong.domain.userhealth.enums;

import com.sesac.boheommong.domain.insurance.enums.InsuranceType;

public enum ChronicDisease {

    HYPERTENSION("고혈압",
            new InsuranceType[]{InsuranceType.LIFE, InsuranceType.HEALTHCARE}),

    DIABETES("당뇨",
            new InsuranceType[]{InsuranceType.CANCER, InsuranceType.SURGERY, InsuranceType.LIFE, InsuranceType.HEALTHCARE}),

    HYPERLIPIDEMIA("고지혈증",
            new InsuranceType[]{InsuranceType.LIFE, InsuranceType.HEALTHCARE}),

    ASTHMA("천식",
            new InsuranceType[]{InsuranceType.SURGERY, InsuranceType.HEALTHCARE}),

    ARTHRITIS("관절염",
            new InsuranceType[]{InsuranceType.SURGERY}),

    STROKE("뇌졸중",
            new InsuranceType[]{InsuranceType.DEMENTIA, InsuranceType.NURSING}),

    ANGINA("협심증",
            new InsuranceType[]{InsuranceType.LIFE, InsuranceType.HEALTHCARE}),

    CANCER("암",
            new InsuranceType[]{InsuranceType.CANCER, InsuranceType.LIFE}),

    HEPATITIS("간염",
            new InsuranceType[]{InsuranceType.HEALTHCARE}),

    HEARTFAILURE("심부전",
            new InsuranceType[]{InsuranceType.LIFE, InsuranceType.HEALTHCARE}),

    MIGRAINE("편두통",
            new InsuranceType[]{InsuranceType.HEALTHCARE}),

    OSTEOPOROSIS("골다공증",
            new InsuranceType[]{InsuranceType.SURGERY, InsuranceType.HEALTHCARE}),

    COPD("COPD(만성폐쇄성폐질환)",
            new InsuranceType[]{InsuranceType.SURGERY, InsuranceType.HEALTHCARE}),

    LIVER_CIRRHOSIS("간경화",
            new InsuranceType[]{InsuranceType.HEALTHCARE, InsuranceType.LIFE}),

    KIDNEY_DISEASE("만성신장질환",
            new InsuranceType[]{InsuranceType.LIFE, InsuranceType.HEALTHCARE}),

    THYROID_DISEASE("갑상선질환",
            new InsuranceType[]{InsuranceType.CANCER, InsuranceType.HEALTHCARE});

    private final String korName;
    private final InsuranceType[] relatedTypes; // 연관 보험카테고리

    ChronicDisease(String korName, InsuranceType[] relatedTypes) {
        this.korName = korName;
        this.relatedTypes = relatedTypes;
    }

    public String getKorName() {
        return korName;
    }

    public InsuranceType[] getRelatedTypes() {
        return relatedTypes;
    }
}
