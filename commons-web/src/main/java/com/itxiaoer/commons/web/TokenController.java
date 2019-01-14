package com.itxiaoer.commons.web;

import com.itxiaoer.commons.core.page.Response;
import com.itxiaoer.commons.core.page.ResponseCode;
import com.itxiaoer.commons.jwt.JwtAuth;
import com.itxiaoer.commons.jwt.JwtToken;
import com.itxiaoer.commons.security.AuthenticationUtils;
import com.itxiaoer.commons.security.JwtTokenContext;
import com.itxiaoer.dis.commons.annotation.Dis;
import com.itxiaoer.dis.commons.annotation.DisInclude;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author : liuyk
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public class TokenController {


    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtTokenContext jwtTokenContext;


    @PostMapping("/login")
    public Response<JwtToken> doLogin(@Valid @RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(loginDto.getLoginName(), loginDto.getPassword());
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final JwtAuth userDetails = (JwtAuth) userDetailsService.loadUserByUsername(loginDto.getLoginName());
        if (userDetails == null) {
            return Response.failure("用户或密码错误", ResponseCode.NOT_FOUNT_CODE);
        }
        return Response.ok(jwtTokenContext.build(userDetails));

    }

    @Dis(expireTime = 2000)
    @PutMapping("/tokens/refresh")
    public Response<JwtToken> refreshToken(@DisInclude @RequestBody JwtToken token) {
        try {
            // 刷新token的值
            JwtToken refresh = jwtTokenContext.refresh(token.getToken());
            JwtAuth user = AuthenticationUtils.getUser();
            this.refresh().accept(refresh, user.getId());
            return Response.ok(refresh);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.failure(" refresh token error. ");
        }
    }

    @PutMapping("/tokens/destroy")
    public Response<Boolean> destroy(HttpServletRequest request) {
        try {
            String id = AuthenticationUtils.getUser().getId();
            SecurityContextHolder.getContext().setAuthentication(null);
            Boolean destroy = jwtTokenContext.destroy(request);
            this.destroy().accept(destroy, id);
            // 刷新token的值
            return Response.ok(destroy);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.failure(" destroy token error. ");
        }
    }

    @GetMapping("/profile")
    public Response<JwtAuth> getProfile() {
        JwtAuth user = AuthenticationUtils.getUser();
        this.profile().accept(user);
        return Response.ok(user);
    }

    public Consumer<JwtAuth> profile() {
        return jwtAuth -> {
        };
    }


    public BiConsumer<JwtToken, String> refresh() {
        return (jwtToken, id) -> {
        };
    }

    public BiConsumer<Boolean, String> destroy() {
        return (success, id) -> {
        };
    }

}
