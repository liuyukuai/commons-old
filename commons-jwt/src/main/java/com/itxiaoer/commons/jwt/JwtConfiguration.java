package com.itxiaoer.commons.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : liuyk
 */
@Configuration
public class JwtConfiguration {

    @Bean
    public JwtBuilder jwtTokenUtil() {
        return new JwtBuilder();
    }

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }
}
