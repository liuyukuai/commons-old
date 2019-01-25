package com.itxiaoer.commons.request;

import com.itxiaoer.commons.jwt.JwtAuth;
import com.itxiaoer.commons.jwt.JwtProperties;
import com.itxiaoer.commons.security.AuthenticationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Optional;

/**
 * @author : liuyk
 */
@Slf4j
public class TokenInterceptor implements ClientHttpRequestInterceptor {

    private final static String TOKEN_HEAD = "Bearer ";

    @Resource
    private JwtProperties jwtProperties;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        try {

            String token = Optional.ofNullable(AuthenticationUtils.getUser()).map(JwtAuth::getToken).orElse("");
            if (log.isDebugEnabled()) {
                log.debug("get  token =  {} ", token);
            }
            if (StringUtils.isNotBlank(token)) {
                headers.add(jwtProperties.getHeader(), TOKEN_HEAD + " " + token);
            }
        } catch (Exception e) {
            log.warn("get token error , message = {} ", e.getMessage());
        }
        return execution.execute(request, body);
    }
}