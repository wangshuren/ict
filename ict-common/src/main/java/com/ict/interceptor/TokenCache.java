package com.ict.interceptor;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/27
 * @Version 1.0
 */
@Component
public class TokenCache {private static final ThreadLocal<String> TOKEN_THREAD_LOCAL = new ThreadLocal<>();
    private static final String ICT_LOGIN_KEY = "ict-token-";

    /**
     * 添加前缀
     *
     * @param
     * @return
     */
    public static String createNewToken() {
        return ICT_LOGIN_KEY + UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取当前请求线程的token
     *
     * @return
     */
    public static String getToken() {
        return TOKEN_THREAD_LOCAL.get();
    }

    /**
     * 将token缓存到当前线程中，在拦截器TokenInterceptor中调用此方式
     *
     * @param token
     */
    public static void addToken(String token) {
        TOKEN_THREAD_LOCAL.set(token);
    }

    /**
     * 删除当前请求线程中的token数据
     */
    public static void remove() {
        TOKEN_THREAD_LOCAL.remove();
    }

}