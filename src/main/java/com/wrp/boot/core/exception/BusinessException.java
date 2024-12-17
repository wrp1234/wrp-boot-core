package com.wrp.boot.core.exception;

/**
 * 业务类异常
 * @author wrp
 * @since 2024年12月17日 14:03
 **/
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public static BusinessException of(String message) {
        return new BusinessException(message);
    }
}
