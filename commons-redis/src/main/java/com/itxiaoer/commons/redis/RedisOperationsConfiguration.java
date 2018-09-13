package com.itxiaoer.commons.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.*;

/**
 * @author : liuyk
 */
@Configuration
public class RedisOperationsConfiguration {

    @Bean
    public <T> HashOperations<String, String, T> hashOperations(RedisTemplate<String, T> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean
    public <T> ValueOperations<String, T> valueOperations(RedisTemplate<String, T> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public <T> ListOperations<String, T> listOperations(RedisTemplate<String, T> redisTemplate) {
        return redisTemplate.opsForList();
    }

    @Bean
    public <T> SetOperations<String, T> setOperations(RedisTemplate<String, T> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    @Bean
    public <T> ZSetOperations<String, T> zSetOperations(RedisTemplate<String, T> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

}
