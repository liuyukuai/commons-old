package com.itxiaoer.commons.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.itxiaoer.commons.core.date.LocalDateTimeUtil;
import com.itxiaoer.commons.jwt.JwtRemoteAuth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
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
                JwtRemoteAuth jwtRemoteAuth = new JwtRemoteAuth(remote.getUsername(), LocalDateTimeUtil.format(remote.getModifyPasswordTime(), LocalDateTimeUtil.DEFAULT_PATTERN));
                JWT_AUTH_CACHE.put(loginName, jwtRemoteAuth);
                return jwtRemoteAuth;
            }
            return null;
        });
    }

    /**
     * 缓存中查询用户信息
     *
     * @param loginName 登录名
     * @param token     token
     * @return 用户信息
     */
    default JwtRemoteAuth loadUserFromCache(String loginName, String token) {
        return Optional.ofNullable(JWT_AUTH_CACHE.getIfPresent(loginName)).orElseGet(() -> {
            if (StringUtils.isBlank(token)) {
                return this.loadUserFromCache(loginName);
            }
            JwtRemoteAuth remote = this.loadUserByUsername(loginName, token);
            if (!Objects.isNull(remote)) {
                JWT_AUTH_CACHE.put(loginName, remote);
                return remote;
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
    default JwtUserDetail loadUserByUsername(String loginName) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("please must overwrite this method.");
    }

    /**
     * 中查询用户信息
     *
     * @param loginName 登录名
     * @param token     token
     * @return 用户信息
     * @throws UsernameNotFoundException e
     */
    default JwtRemoteAuth loadUserByUsername(String loginName, String token) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("please must overwrite this method.");
    }
}
