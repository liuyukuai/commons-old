package com.itxiaoer.commons.security.wx;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author : liuyk
 */
@Configuration
@ComponentScan("com.itxiaoer.commons.security.wx.web")
public class WxConfiguration {

    @Bean
    public RestTemplate wxRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WxProperties wxProperties() {
        return new WxProperties();
    }

    @Bean
    public WxAuthenticationEntryPoint wxAuthenticationEntryPoint() {
        return new WxAuthenticationEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(WxUseDetailServiceImpl.class)
    public WxUseDetailServiceImpl wxUseDetailService() {
        return new WxUseDetailServiceImpl();
    }

    @Bean
    WxAbstractAuthenticationTokenFilter wxAuthenticationTokenFilter() {
        return new WxAbstractAuthenticationTokenFilter();
    }

}
