package com.ict.login.service.impl;

import com.ict.login.dao.UserDao;
import com.ict.login.entity.Permission;
import com.ict.login.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 用户登录认证逻辑
 * @Author wangsr
 * @Date 2021/2/9
 * @Version 1.0
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 需要构造出 org.springframework.security.core.userdetails.User 对象并返回

        if (username == null || "".equals(username)) {
            throw new RuntimeException("用户不能为空");
        }
        //根据用户名查询用户
        User sysUser = userDao.selectByName(username);
        if (sysUser == null) {
            throw new RuntimeException("用户不存在");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (sysUser != null) {
            //获取该用户所拥有的权限
            List<Permission> sysPermissions = userDao.selectListByUser(sysUser.getId());
            // 声明用户授权
            sysPermissions.forEach(sysPermission -> {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(sysPermission.getPermissionCode());
                grantedAuthorities.add(grantedAuthority);
            });
        }
        return new org.springframework.security.core.userdetails.User(sysUser.getAccount(), sysUser.getPassword(), sysUser.getEnabled(), sysUser.getAccountNonExpired(), sysUser.getCredentialsNonExpired(), sysUser.getAccountNonLocked(), grantedAuthorities);

    }
}