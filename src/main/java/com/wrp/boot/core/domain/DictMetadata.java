package com.wrp.boot.core.domain;

/**
 * 字典元信息接口， 所有枚举类都实现它
 * @author wrp
 * @since 2024年12月17日 13:54
 **/
public interface DictMetadata {

    /**
     * 字典值
     */
    int getCode();

    /**
     * 字典描述信息
     */
    String getDescription();
}
