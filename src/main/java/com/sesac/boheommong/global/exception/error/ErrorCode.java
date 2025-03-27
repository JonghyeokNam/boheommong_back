package com.sesac.boheommong.global.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // user
    USER_NOT_FOUND("USER-0000", "해당 회원이 존재하지 않습니다.", ErrorDisplayType.POPUP),
    INVALID_PERMISSION("USER-0001", "허용된 접근이 아닙니다.", ErrorDisplayType.POPUP),

    // user health
    USER_HEALTH_ALREADY_EXISTS("HEALTH-0001", "해당 유저의 건강정보가 이미 존재합니다.", ErrorDisplayType.POPUP),
    USER_HEALTH_NOT_FOUND("HEALTH-0002", "해당 유저의 건강정보가 존재하지 않습니다.", ErrorDisplayType.POPUP),
    USER_HEALTH_INVALID("HEALTH-0003", "유효하지 않은 건강정보입니다.", ErrorDisplayType.POPUP),

    // notification
    NOTIFICATION_SEND_FAIL("NOTIFICATION-0001", "알림 전송 실패", ErrorDisplayType.POPUP),
    NOTIFICATION_NOT_FOUND("NOTIFICATION-0002", "해당 알림이 존재하지 않습니다.", ErrorDisplayType.POPUP),

    // insurance product
    INSURANCE_PRODUCT_NOT_FOUND("INS-0001", "해당 보험 상품이 존재하지 않습니다.", ErrorDisplayType.POPUP),

    // bookmark
    BOOKMARK_NOT_FOUND("BOOKMARK-0001", "해당 북마크가 존재하지 않습니다.", ErrorDisplayType.POPUP),

    ;

    private final String code;
    private final String message;
    private final ErrorDisplayType displayType;
}
