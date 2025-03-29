package com.watermelon.sso.common;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final int code;
    private Integer status;

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(int code, String message, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public ServiceException(int code, String message, Exception e) {
        super(message, e);
        this.code = code;
    }

    public ServiceException(int code, String message, int status, Exception e) {
        super(message, e);
        this.code = code;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
