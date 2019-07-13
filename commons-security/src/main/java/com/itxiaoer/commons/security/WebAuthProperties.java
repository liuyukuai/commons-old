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
@ConfigurationProperties(prefix = "commons.security.access")
@EnableConfigurationProperties
public class WebAuthProperties {

    @Value("${commons.security.enableSession:false}")
    private boolean enableSession;
    /**
     * 不需要登录权限的url
     */
    @Value("${commons.security.access.permitAll:''}")
    private String permitAll;

    /**
     * 权限列表
     */
    private Map<String, Object> roles;

    /**
     * 允许的 origin
     */
    @Value("${commons.security.access.allowedOrigin:*}")
    private String allowedOrigin;


    @Value("${commons.security.access.allowedMethod:GET,OPTIONS,POST,PUT,DELETE}")
    private String allowedMethod;


    @Value("${commons.security.access.allowedHeader:*}")
    private String allowedHeader;

    @Value("${commons.security.client.header:X-CLIENT-ID}")
    private String clientId;
}
