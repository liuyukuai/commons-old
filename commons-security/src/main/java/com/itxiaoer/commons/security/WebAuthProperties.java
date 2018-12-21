package com.itxiaoer.commons.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author : liuyk
 */
@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "spring.auth.access")
public class WebAuthProperties {
    /**
     * 不需要登录权限登录url
     */
    @Value("${spring.auth.access.permitAll:''}")
    private String permitAll;


    private Map<String, String> roles;

    @Value("${spring.auth.access.allowedOrigin:*}")
    private String allowedOrigin;


    @Value("${spring.auth.access.allowedMethod:GET,OPTIONS,POST,PUT,DELETE}")
    private String allowedMethod;


    @Value("${spring.auth.access.allowedHeader:*}")
    private String allowedHeader;

    @Value("${spring.auth.client.header:X-CLIENT-ID}")
    private String clientId;
}
