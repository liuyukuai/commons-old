package com.itxiaoer.commons.wx;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private String secret;
    /**
     * 通讯录秘钥
     */
    @JsonIgnore
    private String addressSecret;

    @JsonIgnore
    @Value("${commons.security.wx.scope:snsapi_base}")
    private String scope;

    @JsonIgnore
    @Value("${commons.security.wx.responseType:code}")
    private String responseType;

    @JsonIgnore
    @Value("${commons.security.wx.state:wx}")
    private String state;

    private String agentId;
    /**
     * 是否跳转页面
     */
    @Value("${commons.security.wx.redirect:true}")
    private boolean redirect;
}
