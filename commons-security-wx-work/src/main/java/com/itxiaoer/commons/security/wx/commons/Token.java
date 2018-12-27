package com.itxiaoer.commons.security.wx.commons;

import lombok.Data;

/**
 * 微信token对象
 *
 * @author : liuyk
 */
@Data
@SuppressWarnings("all")
public class Token {
    private String errcode;

    private String errmsg;

    private String access_token;

    private String expires_in;

}
