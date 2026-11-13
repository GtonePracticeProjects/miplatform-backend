package com.bank.common.exception;

/** 직접 JDBC 사용 시 SQLException 을 감싸는 런타임 예외 */
public class DbException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DbException(String message, Throwable cause) {
        super(message, cause);
    }
}
