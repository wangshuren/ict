package com.ict.interceptor;

import com.ict.annotation.Token;
import com.ict.entity.model.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author wangsr
 * @Date 2021/4/27
 * @Version 1.0
 */
@Component
//@Slf4j
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationInterceptor.class);
    public static final String USER_KEY = "USER_ID";
    public static final String USER_INFO = "USER_INFO";
    private static final String NO_TOKEN_URL = "/notNeedToken";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Token annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Token.class);
        }else{
            return true;
        }
        //没有声明需要权限,或者声明不验证权限
        if(annotation == null || annotation.validate() == false){
            return true;
        }
        //从header中获取token
        String token = request.getHeader("token");
        if(token == null){
            log.info("缺少token，拒绝访问");
            return false;
        }
        //查询token信息
//        User user = redisUtils.get(USER_INFO+token,User.class);
//        if(user == null){
//            log.info("token不正确，拒绝访问");
//            return false;
//        }

        String servletPath = request.getServletPath();
        log.info("reqest URL:[{}], Token is:[{}].", servletPath, token);
        if (!NO_TOKEN_URL.equals(servletPath)) {
            if (token == null || "".equals(token)) {
                return false;
            }
            TokenCache.addToken(token);
        }

        //token校验通过，将用户信息放在request中，供需要用user信息的接口里从token取数据
        request.setAttribute(USER_KEY, "123456");
        User user=new User();
        user.setId(10000);
        user.setUserName("2118724165@qq.com");
        user.setPhoneNumber("15702911111");
        user.setToken(token);
        request.setAttribute(USER_INFO, user);
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (!NO_TOKEN_URL.equals(request.getServletPath())) {
            TokenCache.remove();
        }
    }
}