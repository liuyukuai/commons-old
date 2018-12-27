package com.itxiaoer.commons.security;

import com.itxiaoer.commons.jwt.JwtAuth;
import com.itxiaoer.commons.jwt.JwtBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;


/**
 * @author : liuyk
 */
public class JwAuthenticationTokenFilter extends AbstractAuthenticationTokenFilter {

    @Resource
    private JwtBuilder jwtBuilder;


    @Resource
    private JwtTokenContext jwtTokenContext;

    @Resource
    private JwtUserDetailService jwtUserDetailService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        String authToken = jwtTokenContext.getTokenFromRequest(request);
        if (StringUtils.isNotBlank(authToken)) {
            String loginName = jwtBuilder.getLoginNameFromToken(authToken);

            if (logger.isDebugEnabled()) {
                logger.info("checking authentication " + loginName);
            }

            if (StringUtils.isNotBlank(loginName) && SecurityContextHolder.getContext().getAuthentication() == null) {
                JwtUserDetail jwtRemoteAuth = jwtUserDetailService.loadUserFromCache(loginName, authToken);
                if (jwtTokenContext.validate(authToken, jwtRemoteAuth)) {
                    JwtAuth jwtAuth = jwtBuilder.getJwtAuth(authToken);
                    jwtAuth.setToken(authToken);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtAuth, null, jwtAuth.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    if (logger.isDebugEnabled()) {
                        logger.debug("authenticated user " + loginName + " success , setting security context");
                    }
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    SecurityContextHolder.getContext().setAuthentication(null);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
