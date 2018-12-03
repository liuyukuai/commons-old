package com.itxiaoer.commons.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * @author : liuyk
 */
@Configuration
@SuppressWarnings("all")
public class CrossOriginConfig extends WebMvcConfigurerAdapter {

    @Resource
    private WebAuthProperties webAuthProperties;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(webAuthProperties.getAllowedOrigin())
                .allowedMethods(webAuthProperties.getAllowedMethod().split(","))
                .allowedHeaders(webAuthProperties.getAllowedHeader())
                .allowCredentials(false).maxAge(3600);
    }
}
