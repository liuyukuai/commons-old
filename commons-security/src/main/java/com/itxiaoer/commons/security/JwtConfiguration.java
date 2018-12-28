package com.itxiaoer.commons.security;

import com.itxiaoer.commons.security.cache.CacheService;
import com.itxiaoer.commons.security.cache.LocalCacheServiceImpl;
import com.itxiaoer.commons.security.cache.RedisCacheServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author : liuyk
 */
@Configuration
public class JwtConfiguration {

    @Bean
    public JwtTokenContext jwtTokenContext() {
        return new JwtTokenContext();
    }

    @Bean
    @ConditionalOnMissingBean(AbstractAuthenticationTokenFilter.class)
    public AbstractAuthenticationTokenFilter abstractAuthenticationTokenFilter() {
        return new JwAuthenticationTokenFilter();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }


    @Bean
    @ConditionalOnProperty(value = "commons.security.jwt.storeType", havingValue = "redis")
    public CacheService redisCacheService() {
        return new RedisCacheServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(value = "commons.security.jwt.storeType", havingValue = "local", matchIfMissing = true)
    public CacheService localCacheService() {
        return new LocalCacheServiceImpl();
    }
}
