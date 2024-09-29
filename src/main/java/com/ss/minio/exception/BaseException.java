package com.ss.minio.exception;

import org.springframework.http.HttpStatus;

/**
 * 自定义异常
 */
public class BaseException extends RuntimeException {

    private Integer code;

    public BaseException() {
    }

    public BaseException(Throwable throwable) {
        super(throwable);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public BaseException(String msg) {
        super(msg);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public BaseException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }


    public void setCode(Integer code) {
        this.code = code;
    }
}
