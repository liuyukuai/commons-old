package com.itxiaoer.commons.wx.js;

import com.itxiaoer.commons.core.util.Md5Utils;
import com.itxiaoer.commons.wx.WxAddressService;
import com.itxiaoer.commons.wx.WxProperties;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Random;

/**
 * js token
 *
 * @author : liuyk
 */
public class JsTokenService {

    @Resource
    private WxAddressService wxAddressService;

    @Resource
    private WxProperties wxProperties;

    public JsSign sign(String url) {
        String jsToken = wxAddressService.getJsToken();
        long timestamp = Instant.now().toEpochMilli();
        StringBuilder sb = new StringBuilder();
        int noncestr = new Random().nextInt(1000);

        sb.append("jsapi_ticket=").append(jsToken)
                .append("noncestr=").append(noncestr)
                .append("timestamp=").append(timestamp)
                .append("url=").append(url);
        String sign = Md5Utils.digestMD5(sb.toString());
        return new JsSign(wxProperties.getAppId(), jsToken, String.valueOf(timestamp), String.valueOf(noncestr), url, sign);
    }

}
