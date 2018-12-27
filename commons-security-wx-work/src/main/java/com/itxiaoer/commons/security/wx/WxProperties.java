package com.itxiaoer.commons.security.wx;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : liuyk
 */
@Data
@SuppressWarnings("WeakerAccess")
@ConfigurationProperties(prefix = "commons.security.wx")
public class WxProperties {
    private String appId;
    private String secret;
    @Value("${commons.security.wx.scope:snsapi_base}")
    private String scope;
    @Value("${commons.security.wx.responseType:code}")
    private String responseType;
    @Value("${commons.security.wx.state:wx}")
    private String state;
    private String agentId;
}
