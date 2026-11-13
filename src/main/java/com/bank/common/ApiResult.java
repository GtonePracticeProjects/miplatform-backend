package com.bank.common;

import java.io.Serializable;

/** Ajax/fetch 공통 응답 포맷: { success, code, message, data } */
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String code;
    private String message;
    private T data;

    public static <T> ApiResult<T> ok(T data) {
        ApiResult<T> r = new ApiResult<T>();
        r.success = true;
        r.code = "0000";
        r.message = "OK";
        r.data = data;
        return r;
    }

    public static <T> ApiResult<T> fail(String code, String message) {
        ApiResult<T> r = new ApiResult<T>();
        r.success = false;
        r.code = code;
        r.message = message;
        return r;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
