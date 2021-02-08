package com.ict.login.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @Author: wsr
 * @Description: Spring Security核心配置：WebSecurityConfig
 * @Date Create in 2021/02/08 17:16
 */
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置认证方式等
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http的相关配置，包括登入登出、异常处理、会话管理等
        super.configure(http);
    }
}
