package com.wrp.boot.core.domain;

import java.time.LocalDateTime;

/**
 * 实体类的基类，所有表映射类都继承它
 * @author wrp
 * @since 2024年12月17日 13:56
 **/
public class BaseEntity {
    private Integer id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
