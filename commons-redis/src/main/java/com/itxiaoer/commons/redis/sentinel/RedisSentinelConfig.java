package com.itxiaoer.commons.redis.sentinel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * has sentinel
 *
 * @author : liuyk
 */
@Getter
@Setter
@Configuration
@SuppressWarnings("all")
@ConditionalOnProperty("spring.redis.sentinel.master")
@ConfigurationProperties("spring.redis.sentinel")
public class RedisSentinelConfig extends CachingConfigurerSupport {
    /**
     * sentinel name
     */
    private String master;
    /**
     * sentinel nodes
     */
    private String nodes;


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration(this.master, Stream.of(nodes.split(",")).collect(Collectors.toSet()));
        return new JedisConnectionFactory(sentinelConfig);
    }

    @Bean
    public <T> RedisTemplate<String, T> redisTemplate() {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

}
