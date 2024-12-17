package com.wrp.boot.core.context;

import org.springframework.core.NamedThreadLocal;

/**
 * 当前用户上下文
 * @author wrp
 * @since 2024年12月17日 15:11
 **/
public class UserContext {
    private static final ThreadLocal<UserInfo> USER_INFO_THREAD_LOCAL = new NamedThreadLocal<>("userContext");

    public static void add(UserInfo userInfo) {
        USER_INFO_THREAD_LOCAL.set(userInfo);
    }

    public static UserInfo getCurrentUser() {
        return USER_INFO_THREAD_LOCAL.get();
    }

    public static void remove() {
        USER_INFO_THREAD_LOCAL.remove();
    }
}
