package com.itxiaoer.commons.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : liuyk
 */
@Configuration
public class TokenConfig {

    @Bean
    public TokenController tokenController() {
        return new TokenController();
    }
}
