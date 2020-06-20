package com.wuxian.janus;

import lombok.Getter;

public class ErrorCodeException extends RuntimeException {

    @Getter
    private String errorCode;

    @Getter
    private String detail;

    public ErrorCodeException(String errorCode, String message, String detail) {
        super(message);
        this.errorCode = errorCode;
        this.detail = detail;
    }
}
