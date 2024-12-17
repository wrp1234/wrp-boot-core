package com.wrp.boot.core.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 分页查询的基类
 * @author wrp
 * @since 2024年12月17日 14:31
 **/
public class PageQuery<T> {
    @NotNull(message = "起始页码不能为空")
    @Min(value = 1,message = "起始页码需大于1")
    private final Long pageNo;
    @NotNull(message = "分页大小不能为空")
    @Max(value = 50,message = "分页大小不能大于50")
    private final Long pageSize ;
    private final T data;

    public PageQuery(Long pageNo, Long pageSize, T data) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.data = data;
    }

    public Long getPageNo() {
        return pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public T getData() {
        return data;
    }
}
