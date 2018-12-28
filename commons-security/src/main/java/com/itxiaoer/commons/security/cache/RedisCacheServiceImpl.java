package com.itxiaoer.commons.security.cache;

import com.itxiaoer.commons.jwt.JwtToken;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author : liuyk
 */
public class RedisCacheServiceImpl implements CacheService {

    @Resource
    private ValueOperations<String, JwtToken> valueOperations;

    @Override
    public Boolean setIfAbsent(String key, JwtToken token) {
        return valueOperations.setIfAbsent(key, token);
    }

    @Override
    public JwtToken get(String key) {
        return valueOperations.get(key);
    }

    @Override
    public void set(String key, JwtToken value) {
        valueOperations.set(key, value);
    }

    @Override
    public Boolean expireAt(String key, Date date) {
        return valueOperations.getOperations().expireAt(key, date);
    }
}
