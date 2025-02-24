package com.sesac.boheommong.global.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // user
    USER_NOT_FOUND("USER-0000", "해당 회원이 존재하지 않습니다.", ErrorDisplayType.POPUP),
    USER_ALREADY_EXISTS("USER-0001", "이미 존재하는 회원입니다.", ErrorDisplayType.POPUP),

    // [UserHealth 관련 에러]
    USER_HEALTH_ALREADY_EXISTS("HEALTH-0001", "해당 유저의 건강정보가 이미 존재합니다.", ErrorDisplayType.POPUP),
    USER_HEALTH_NOT_FOUND("HEALTH-0002", "해당 유저의 건강정보가 존재하지 않습니다.", ErrorDisplayType.POPUP),
    USER_HEALTH_INVALID("HEALTH-0003", "유효하지 않은 건강정보입니다.", ErrorDisplayType.POPUP),
    ;


    private final String code;
    private final String message;
    private final ErrorDisplayType displayType;
}
