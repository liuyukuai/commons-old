package com.itxiaoer.commons.auth.security;

import com.itxiaoer.commons.jwt.JwtAuth;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取当前登录用户
 *
 * @author : liuyk
 */
@SuppressWarnings("unused")
public final class AuthenticationUtils {

    public static JwtAuth getUser() {
        return (JwtAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
