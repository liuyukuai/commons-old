package com.itxiaoer.commons.security.wx;

/**
 * @author : liuyk
 */
@SuppressWarnings("all")
public class WxConstants {

    public static final String WX_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

    public static final String SCAN_URL = "https://open.work.weixin.qq.com/wwopen/sso/qrConnect?appid=%s&agentid=%s&redirect_uri=%s&state=%s";

    public static final String WX_USER_ID_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=%s&userid=%s";

    public static final String WX_USER_BY_CODE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s";

    public static final String WX_USER_TAG_URL = "https://qyapi.weixin.qq.com/cgi-bin/tag/list?access_token=%s";

    public static final String WX_USER_OF_TAG_URL = "https://qyapi.weixin.qq.com/cgi-bin/tag/get?access_token=%s&tagid=%s";
}
