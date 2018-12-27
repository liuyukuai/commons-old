package com.itxiaoer.commons.security.wx;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;


/**
 * 拦截没有权限
 *
 * @author : liuyk
 */
public class WxAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;


    @Resource
    private WxProperties wxProperties;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        StringBuffer url = request.getRequestURL();
        if (request.getQueryString() != null) {
            url.append('?');
            url.append(request.getQueryString());
        }
        response.sendRedirect(String.format(WxConstants.SCAN_URL, wxProperties.getAppId(), wxProperties.getAgentId(), URLEncoder.encode(url.toString()), wxProperties.getState()));
    }
}
