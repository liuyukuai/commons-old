package com.itxiaoer.commons.wx;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author : liuyk
 */
@Data
@SuppressWarnings("WeakerAccess")
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "commons.security.wx")
public class WxProperties {

    private String appId;

    private String secret;
    /**
     * 通讯录秘钥
     */
    private String addressSecret;

    @Value("${commons.security.wx.scope:snsapi_base}")
    private String scope;

    @Value("${commons.security.wx.responseType:code}")
    private String responseType;

    @Value("${commons.security.wx.state:wx}")
    private String state;

    private String agentId;
    /**
     * 是否跳转页面
     */
    @Value("${commons.security.wx.redirect:true}")
    private boolean redirect;
}
