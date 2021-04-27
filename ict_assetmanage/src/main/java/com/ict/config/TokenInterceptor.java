package com.ict.config;

import com.ict.util.TokenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 请求头中的token数据缓存到cache中
 * @Author wangsr
 * @Date 2021/4/27
 * @Version 1.0
 */
public class TokenInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    private static final String NO_TOKEN_URL = "/notNeedToken";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = request.getHeader("token");
        String servletPath = request.getServletPath();
        logger.info("reqest URL:[{}], Token is:[{}].", servletPath, token);
        if (!NO_TOKEN_URL.equals(servletPath)) {
            if (token == null || "".equals(token)) {
                return false;
            }
            TokenCache.addToken(token);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (!NO_TOKEN_URL.equals(request.getServletPath())) {
            TokenCache.remove();
        }
    }
}