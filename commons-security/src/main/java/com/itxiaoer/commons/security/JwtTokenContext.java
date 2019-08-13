package com.itxiaoer.commons.security;

import com.itxiaoer.commons.core.util.Md5Utils;
import com.itxiaoer.commons.jwt.JwtAuth;
import com.itxiaoer.commons.jwt.JwtBuilder;
import com.itxiaoer.commons.jwt.JwtProperties;
import com.itxiaoer.commons.jwt.JwtToken;
import com.itxiaoer.commons.security.cache.CacheService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * @author : liuyk
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JwtTokenContext {

    private final static String TOKEN_HEAD = "Bearer ";

    @Resource
    private JwtBuilder jwtBuilder;

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private CacheService cacheService;

    @Resource
    JwtUserDetailService jwtUserDetailService;

    public JwtToken build(JwtAuth userDetails) {
        return jwtBuilder.encode(userDetails);
    }

    public JwtToken refresh(String token) {
        JwtUserDetail jwtUserDetail = jwtUserDetailService.loadUserFromCache(jwtBuilder.get(token, "loginName"), token);
        //判断是否过期
        if (!this.validate(token, jwtUserDetail)) {
            throw new IllegalArgumentException("token is expired.");
        }
        // 将原来旧的token加入黑名单
        String key = jwtProperties.getPrefix() + Md5Utils.digestMD5(token);
        Date expirationDateFromToken = jwtBuilder.expirationDate(token);
        Boolean ifAbsent = cacheService.setIfAbsent(key, new JwtToken(token, expirationDateFromToken.getTime(), Instant.now().toEpochMilli(), JwtToken.Operation.refresh.getValue()));
        if (ifAbsent != null && ifAbsent) {
            //设置过期时间
            cacheService.expireAt(key, expirationDateFromToken);
        }
        // 刷新token的值
        return this.jwtBuilder.refresh(token);
    }


    public String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(jwtProperties.getHeader());
        return this.getTokenFromHeader(authHeader);
    }

    public String getTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith(TOKEN_HEAD)) {
            return authHeader.substring(TOKEN_HEAD.length());
        }
        return "";
    }


    public Boolean destroy(HttpServletRequest request) {
        String token = this.getTokenFromRequest(request);
        String key = jwtProperties.getPrefix() + Md5Utils.digestMD5(token);
        Date expirationDateFromToken = jwtBuilder.expirationDate(token);
        cacheService.set(key, new JwtToken(token, expirationDateFromToken.getTime(), Instant.now().toEpochMilli(), JwtToken.Operation.destroy.getValue()));
        //设置过期时间
        cacheService.expireAt(key, expirationDateFromToken);
        return true;
    }

    /**
     * 校验token是否合法
     *
     * @param token       token的值
     * @param userDetails 用户信息
     * @return true:合法，false：非法
     */
    public Boolean validate(String token, JwtUserDetail userDetails) {
        if (StringUtils.isBlank(token) || userDetails == null) {
            return false;
        }
        String key = jwtProperties.getPrefix() + Md5Utils.digestMD5(token);
        JwtToken jwtToken = cacheService.get(key);
        // 判断是否在黑名单中
        if (jwtToken != null) {
            //销毁状态
            if (JwtToken.Operation.destroy.getValue() == jwtToken.getType()) {
                return false;
            }

            Long refreshTime = jwtToken.getRefreshTime();
            if (refreshTime != null && jwtToken.getType() == JwtToken.Operation.refresh.getValue()) {
                // 刷新token二分钟内可以使用
                return Instant.now().toEpochMilli() - refreshTime < 1000 * 60 * 2;
            }
        }
        final Instant created = jwtBuilder.createdTime(token);
        final String loginName = jwtBuilder.get(token, "loginName");
        LocalDateTime modifyPasswordTime = userDetails.getModifyPasswordTime();
        if (Objects.isNull(modifyPasswordTime)) {
            return Objects.equals(loginName, userDetails.getUsername())
                    && !jwtBuilder.isExpired(token);
        }
        return Objects.equals(loginName, userDetails.getUsername())
                && !jwtBuilder.isExpired(token) && validate(created, modifyPasswordTime);
    }

    private Boolean validate(Instant created, LocalDateTime lastPasswordReset) {
        return lastPasswordReset == null || created.isAfter(Instant.from(lastPasswordReset));
    }
}
