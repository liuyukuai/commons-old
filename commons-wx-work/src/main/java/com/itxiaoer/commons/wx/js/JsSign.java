package com.itxiaoer.commons.wx.js;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : liuyk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsSign {

    private String appId;
    private String token;
    private String timestamp;
    private String noncestr;
    private String url;
    private String sign;
}
