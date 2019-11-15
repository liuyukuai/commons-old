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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author : liuyk
 */
@Slf4j
@RestController
@SuppressWarnings({"unused", "WeakerAccess"})
public class TokenController {


    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtTokenContext jwtTokenContext;


    @PostMapping("/login")
    public Response<Object> doLogin(@Valid @RequestBody LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(loginDto.getLoginName(), loginDto.getPassword());
            final Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final JwtAuth userDetails = (JwtAuth) userDetailsService.loadUserByUsername(loginDto.getLoginName());
            this.doLogin().accept(userDetails, loginDto);
            Lockable.COUNTER.remove(loginDto.getLoginName());
            return Response.ok(jwtTokenContext.build(userDetails));
        } catch (BadCredentialsException e) {
            if (this.userDetailsService instanceof Lockable) {
                Lockable lockable = (Lockable) userDetailsService;
                String loginName = loginDto.getLoginName();
                AtomicInteger integer = Lockable.COUNTER.get(loginName);
                if (Objects.isNull(integer)) {
                    integer = new AtomicInteger(1);
                    Lockable.COUNTER.put(loginName, integer);
                } else {
                    int i = integer.incrementAndGet();
                    Lockable.COUNTER.put(loginName, integer);
                    if (i >= lockable.wrongTimes()) {
                        lockable.lock(loginDto.getLoginName());
                        Lockable.COUNTER.remove(loginName);
                    }
                }
                return Response.build(integer.get(), ResponseCode.USER_LOGIN_PASSWORD_INVALID.getCode(), ResponseCode.USER_LOGIN_PASSWORD_INVALID.getMessage());
            }
            return Response.failure(ResponseCode.USER_LOGIN_PASSWORD_INVALID);
        }
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

    public BiConsumer<JwtAuth, LoginDto> doLogin() {
        return (auth, dto) -> {
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
