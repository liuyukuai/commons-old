package com.itxiaoer.commons.security.wx;

import com.itxiaoer.commons.security.AbstractAuthenticationTokenFilter;
import com.itxiaoer.commons.security.JwtUserDetailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
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
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new WxAuthenticationEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(JwtUserDetailService.class)
    public JwtUserDetailService wxUseDetailService() {
        return new WxUseDetailServiceImpl();
    }

    @Bean
    AbstractAuthenticationTokenFilter abstractAuthenticationTokenFilter() {
        return new WxAbstractAuthenticationTokenFilter();
    }

}
