package com.itxiaoer.commons.security.cache;

import com.itxiaoer.commons.jwt.JwtToken;

import java.util.Date;

/**
 * @author : liuyk
 */
@SuppressWarnings("UnusedReturnValue")
public interface CacheService {
    /**
     * set key
     *
     * @param key   key
     * @param token token
     * @return boolean
     */
    Boolean setIfAbsent(String key, JwtToken token);

    /**
     * get token
     *
     * @param key key
     * @return token
     */
    JwtToken get(String key);

    /**
     * set token
     *
     * @param key   key
     * @param token token
     */
    void set(String key, JwtToken token);

    /**
     * set token
     *
     * @param key  key
     * @param date date
     * @return boolean
     */
    Boolean expireAt(String key, Date date);
}
