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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author : liuyk
 */
@Slf4j
@RestController
public class TokenController {


    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtTokenContext jwtTokenContext;


    @PostMapping("/login")
    public Response<JwtToken> login(@Valid @RequestBody LoginDto loginDto) {
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
    public Response<JwtToken> refresh(@DisInclude @RequestBody JwtToken token) {
        try {
            // 刷新token的值
            return Response.ok(jwtTokenContext.refresh(token.getToken()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.failure(" refresh token error. ");
        }
    }

    @PutMapping("/tokens/destroy")
    public Response<Boolean> destroy(HttpServletRequest request) {
        try {
            SecurityContextHolder.getContext().setAuthentication(null);
            // 刷新token的值
            return Response.ok(jwtTokenContext.destroy(request));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Response.failure(" destroy token error. ");
        }
    }

    @GetMapping("/profile")
    public Response<JwtAuth> profile() {
        return Response.ok(AuthenticationUtils.getUser());
    }

}
