package com.itxiaoer.commons.wx;

import com.itxiaoer.commons.wx.js.JsTokenController;
import com.itxiaoer.commons.wx.js.JsTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author : liuyk
 */
@Configuration
@ComponentScan("com.itxiaoer.commons.wx")
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
    public WxAddressService wxAddressService() {
        return new WxAddressService();
    }

    @Bean
    public WxLoginService wxLoginService() {
        return new WxLoginService();
    }

    @Bean
    public JsTokenService jsTokenService() {
        return new JsTokenService();
    }

    @Bean
    public JsTokenController jsTokenController() {
        return new JsTokenController();
    }

}
