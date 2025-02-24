package com.sesac.boheommong.domain.insurance.enums;

public enum InsuranceType {
    CANCER("암"),
    SURGERY("수술/입원"),
    LIFE("종신"),
    DRIVER("운전자/상해"),
    FIRE("주택화재"),
    DENTAL("치아"),
    DEMENTIA("치매"),
    NEWBORN("신생아"),
    HEALTHCARE("실손의료비"),
    CHILD("어린이보험"),
    PET("반려동물보험"),
    NURSING("간병보험"),
    TRAVEL("여행자보험"),
    ETC("기타");

    private final String korName;

    InsuranceType(String korName) {
        this.korName = korName;
    }

    public String getKorName() {
        return korName;
    }
}
