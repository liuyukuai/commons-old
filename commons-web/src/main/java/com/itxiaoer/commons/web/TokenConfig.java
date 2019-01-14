package com.itxiaoer.commons.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : liuyk
 */
@Configuration
public class TokenConfig {

    @Bean
    @ConditionalOnMissingBean(TokenController.class)
    public TokenController tokenController() {
        return new TokenController();
    }
}
