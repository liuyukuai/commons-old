package com.itxiaoer.commons.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.itxiaoer.commons.jwt.JwtRemoteAuth;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author : liuyk
 */
public interface JwtUserDetailService extends UserDetailsService {


    Cache<String, JwtRemoteAuth> JWT_AUTH_CACHE = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .maximumSize(5000)
            .build();

    /**
     * 缓存中查询用户信息
     *
     * @param loginName 登录名
     * @return 用户信息
     */
    default JwtRemoteAuth loadUserFromCache(String loginName) {
        return Optional.ofNullable(JWT_AUTH_CACHE.getIfPresent(loginName)).orElseGet(() -> {
            JwtUserDetail remote = this.loadUserByUsername(loginName);
            if (!Objects.isNull(remote)) {
                JwtRemoteAuth jwtRemoteAuth = new JwtRemoteAuth(remote.getUsername(), remote.getModifyPasswordTime());
                JWT_AUTH_CACHE.put(loginName, jwtRemoteAuth);
                return jwtRemoteAuth;
            }
            return null;
        });
    }

    /**
     * 中查询用户信息
     *
     * @param loginName 登录名
     * @return 用户信息
     * @throws UsernameNotFoundException e
     */
    @Override
    JwtUserDetail loadUserByUsername(String loginName) throws UsernameNotFoundException;
}
