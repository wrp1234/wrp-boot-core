package com.wrp.boot.core.request;

import com.wrp.boot.core.domain.DictMetadata;

/**
 * 响应状态码字典类
 * @author wrp
 * @since 2024年12月17日 13:58
 **/
public enum ResultStatus implements DictMetadata {
    SUCCESS(200, "成功"),
    CLIENT_ERROR(400, "客户端异常"),
    SERVER_ERROR(500, "服务端异常")
    ;
    private final int code;
    private final String description;

    ResultStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
