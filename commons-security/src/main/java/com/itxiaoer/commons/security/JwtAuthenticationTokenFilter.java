package com.itxiaoer.commons.security;

import com.itxiaoer.commons.jwt.JwtAuth;
import com.itxiaoer.commons.jwt.JwtBuilder;
import com.itxiaoer.commons.jwt.JwtProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

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
@SuppressWarnings("SpringJavaAutowiringInspection")
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

//    @Resource
//    private UserDetailsService userDetailsService;

    @Resource
    private JwtBuilder jwtBuilder;

    @Resource
    private JwtProperties jwtProperties;

    private final static String TOKEN_HEAD = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(jwtProperties.getHeader());
        if (authHeader != null && authHeader.startsWith(TOKEN_HEAD)) {
            final String authToken = authHeader.substring(TOKEN_HEAD.length());
            String loginName = jwtBuilder.getLoginNameFromToken(authToken);

            logger.info("checking authentication " + loginName);

            if (StringUtils.isNotBlank(loginName) && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 如果我们足够相信token中的数据，也就是我们足够相信签名token的secret的机制足够好
                // 这种情况下，我们可以不用再查询数据库，而直接采用token中的数据
                // 本例中，我们还是通过Spring Security的 @UserDetailsService 进行了数据查询
                // 但简单验证的话，你可以采用直接验证token是否合法来避免昂贵的数据查询
//                UserDetails userDetails = this.userDetailsService.loadUserByUsername(loginName);

//                if (jwtBuilder.validate(authToken, userDetails)) {
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                            userDetails, null, userDetails.getAuthorities());
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
//                            request));
//                    logger.info("authenticated user " + loginName + ", setting security context");
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }

                if (jwtBuilder.validate(authToken)) {
                    JwtAuth jwtAuth = jwtBuilder.getJwtAuth(authToken);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtAuth, null, jwtAuth.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    logger.info("authenticated user " + loginName + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
