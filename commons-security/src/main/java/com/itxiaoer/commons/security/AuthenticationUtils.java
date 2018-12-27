package com.itxiaoer.commons.security;

import com.itxiaoer.commons.jwt.JwtAuth;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * 获取当前登录用户
 *
 * @author : liuyk
 */
@SuppressWarnings("unused")
public final class AuthenticationUtils {

    public static JwtAuth getUser() {
        return (JwtAuth) Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map(Authentication::getPrincipal).orElse(null);
    }
}
