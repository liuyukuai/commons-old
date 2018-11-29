package com.itxiaoer.commons.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : liuyk
 */
@Configuration
public class JwtConfiguration {

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil();
    }

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }
}
