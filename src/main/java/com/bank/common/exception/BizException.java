package com.bank.common.exception;

/** 업무 예외. errorCode 는 대외 표준 에러코드 체계(E###)를 따른다. */
public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String errorCode;

    public BizException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
