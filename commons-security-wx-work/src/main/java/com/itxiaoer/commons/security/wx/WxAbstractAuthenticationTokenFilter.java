package com.itxiaoer.commons.security.wx;

import com.itxiaoer.commons.security.JwAuthenticationTokenFilter;
import com.itxiaoer.commons.security.JwtUserDetail;
import com.itxiaoer.commons.security.JwtUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @author : liuyk
 */
@Slf4j
public class WxAbstractAuthenticationTokenFilter extends JwAuthenticationTokenFilter {

    @Resource
    private JwtUserDetailService jwtUserDetailService;

    @Resource
    private WxProperties wxProperties;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StringBuffer url = request.getRequestURL();
        String state = request.getParameter("state");
        String code = request.getParameter("code");
        // 处理微信回调
        if (log.isDebugEnabled()) {
            log.debug("{} , code = {} , state = {} ", url, code, state);
        }
        String authToken = getJwtTokenContext().getTokenFromRequest(request);

        if (authentication == null && StringUtils.isBlank(authToken) && Objects.equals(wxProperties.getState(), state) && StringUtils.isNotBlank(code)) {
            JwtUserDetail jwtRemoteAuth = jwtUserDetailService.loadUserFromCache(code);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(jwtRemoteAuth, null, jwtRemoteAuth.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            if (logger.isDebugEnabled()) {
                logger.debug("authenticated user " + jwtRemoteAuth.getLoginName() + " success , setting security context");
            }
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            chain.doFilter(request, response);
        } else {
            super.doFilterInternal(request, response, chain);
        }
    }
}
