package com.itxiaoer.commons.security.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.itxiaoer.commons.jwt.JwtProperties;
import com.itxiaoer.commons.jwt.JwtToken;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author : liuyk
 */
public class LocalCacheServiceImpl implements CacheService, CommandLineRunner {

    @Resource
    private JwtProperties jwtProperties;

    private Cache<String, JwtToken> jwtTokenCache;

    @Override
    public Boolean setIfAbsent(String key, JwtToken token) {
        jwtTokenCache.put(key, token);
        return true;
    }

    @Override
    public JwtToken get(String key) {
        return jwtTokenCache.getIfPresent(key);
    }

    @Override
    public void set(String key, JwtToken value) {
        jwtTokenCache.put(key, value);
    }

    @Override
    public Boolean expireAt(String key, Date date) {
        return null;
    }

    @Override
    public void run(String... args) {
        jwtTokenCache = Caffeine.newBuilder()
                .expireAfterWrite(jwtProperties.getExpiration(), TimeUnit.SECONDS)
                .build();
    }
}
