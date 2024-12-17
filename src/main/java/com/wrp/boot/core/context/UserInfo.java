package com.wrp.boot.core.context;

/**
 * 用户信息
 * @author wrp
 * @since 2024年12月17日 15:11
 **/
public class UserInfo {

    private final Long userId;
    private final String username;
    private final String phone;

    public UserInfo(Long userId, String username, String phone) {
        this.userId = userId;
        this.username = username;
        this.phone = phone;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }
}
