package com.itxiaoer.commons.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
}
