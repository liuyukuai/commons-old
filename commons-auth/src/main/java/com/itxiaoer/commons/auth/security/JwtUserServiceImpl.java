package com.itxiaoer.commons.auth.security;

import com.itxiaoer.commons.jwt.JwtAuth;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户信息获取类
 *
 * @author : liuyk
 */
@SuppressWarnings("unused")
public interface JwtUserServiceImpl extends UserDetailsService {

    /**
     * Ø
     * 通过登录名获取用户信息
     *
     * @param loginName 登录名
     * @return 登录用户信息
     * @throws UsernameNotFoundException
     */
    @Override
    JwtAuth loadUserByUsername(String loginName) throws UsernameNotFoundException;
}
