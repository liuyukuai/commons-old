package com.itxiaoer.commons.security.wx;

import com.itxiaoer.commons.security.JwtAuthenticationEntryPoint;
import com.itxiaoer.commons.wx.WxConstants;
import com.itxiaoer.commons.wx.WxProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Objects;


/**
 * 拦截没有权限
 *
 * @author : liuyk
 */
@Slf4j
public class WxAuthenticationEntryPoint extends JwtAuthenticationEntryPoint {

    private static final String XML_REQUESTED_WITH = "XMLHttpRequest";

    @Resource
    private WxProperties wxProperties;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 判断请求方式
        String header = request.getHeader("X-Requested-With");
        if (!wxProperties.isRedirect() || Objects.equals(header, XML_REQUESTED_WITH)) {
            super.commence(request, response, authException);
        } else {
            StringBuffer url = request.getRequestURL();
            String queryString = request.getQueryString();
            if (StringUtils.isNotBlank(queryString)) {
                url.append('?');
                url.append(queryString);
            }
            response.sendRedirect(String.format(WxConstants.SCAN_URL, wxProperties.getAppId(), wxProperties.getAgentId(), URLEncoder.encode(url.toString(), "UTF-8"), wxProperties.getState()));
        }
    }
}
