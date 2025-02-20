package com.sesac.boheommong.global.exception;

import com.sesac.boheommong.global.exception.error.ErrorCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;
    private String customErrorMessage;

    private BaseException(ErrorCode code) {
        this.errorCode = code;
    }

    private BaseException(ErrorCode code, final String message) {
        this.errorCode = code;
        this.customErrorMessage = message;
    }

    public boolean hasCustomMessage(){
        return customErrorMessage != null;
    }

    public static BaseException from(ErrorCode errorCode) {
        return new BaseException(errorCode);
    }

    public static BaseException from(ErrorCode errorCode, final String customErrorMessage){
        return new BaseException(errorCode, customErrorMessage);
    }
}
