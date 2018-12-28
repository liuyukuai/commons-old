package com.itxiaoer.commons.security.cache;

import com.itxiaoer.commons.jwt.JwtToken;

import java.util.Date;

/**
 * @author : liuyk
 */
public interface CacheService {

    Boolean setIfAbsent(String key, JwtToken token);

    JwtToken get(String key);

    void set(String key, JwtToken token);

    Boolean expireAt(String key, Date date);
}
