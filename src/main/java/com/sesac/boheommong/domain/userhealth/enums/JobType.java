package com.sesac.boheommong.domain.userhealth.enums;

public enum JobType {
    OFFICE("사무직"),
    DELIVERY("배달/라이더"),
    CONSTRUCTION("건설현장"),
    SELF_EMPLOYED("자영업"),
    STUDENT("학생"),
    HOUSEWIFE("주부"),
    UNEMPLOYED("무직");

    private final String korName;

    JobType(String korName) {
        this.korName = korName;
    }

    public String getKorName() {
        return korName;
    }
}
