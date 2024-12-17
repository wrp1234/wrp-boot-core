package com.wrp.boot.core.request;

/**
 * 请求响应
 * @author wrp
 * @since 2024年12月17日 13:45
 **/
public class Result<T> {

    private final int code;
    private final String message;
    private final T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return newInstance(ResultStatus.SUCCESS.getCode(),
                ResultStatus.SUCCESS.getDescription(), data);
    }

    /**
     * 客户端异常
     */
    public static <T> Result<T> error(String message) {
        return newInstance(ResultStatus.CLIENT_ERROR.getCode(), message, null);
    }

    /**
     * 服务端异常
     */
    public static <T> Result<T> serverError(String message) {
        return newInstance(ResultStatus.SERVER_ERROR.getCode(), message, null);
    }

    private static <T> Result<T> newInstance(int code, String message, T data) {
        return new Result<>(code, message, data);
    }
}
