package com.itxiaoer.commons.security;

import com.itxiaoer.commons.core.date.LocalDateTimeUtil;
import com.itxiaoer.commons.core.util.Md5Utils;
import com.itxiaoer.commons.jwt.JwtAuth;
import com.itxiaoer.commons.jwt.JwtBuilder;
import com.itxiaoer.commons.jwt.JwtProperties;
import com.itxiaoer.commons.jwt.JwtRemoteAuth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;

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
    private ValueOperations<String, JwtRemoteAuth> valueOperations;

    public String build(JwtAuth userDetails) {
        String token = jwtBuilder.build(userDetails);
        String key = jwtProperties.getPrefix() + Md5Utils.digestMD5(token);
        valueOperations.setIfAbsent(key, new JwtRemoteAuth());
        Instant now = Instant.now();
        now = now.plusSeconds(jwtProperties.getExpiration());
        valueOperations.getOperations().expireAt(key, new Date(now.toEpochMilli()));
        return token;
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


    public Boolean remove(String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        return valueOperations.getOperations().delete(jwtProperties.getPrefix() + Md5Utils.digestMD5(token));
    }

    @Async
    public Boolean updateTime(String token) {
        String key = jwtProperties.getPrefix() + Md5Utils.digestMD5(token);
        Instant now = Instant.now();
        now = now.plusSeconds(jwtProperties.getExpiration());
        return valueOperations.getOperations().expireAt(key, new Date(now.toEpochMilli()));
    }


    /**
     * 校验token是否合法
     *
     * @param token       token的值
     * @param userDetails 用户信息
     * @return true:合法，false：非法
     */
    public Boolean validate(String token, JwtRemoteAuth userDetails) {
        if (StringUtils.isBlank(token) || userDetails == null) {
            return false;
        }
        final Instant created = jwtBuilder.getCreatedTimeFromToken(token);
        final String loginName = jwtBuilder.getLoginNameFromToken(token);
        String modifyPasswordTime = userDetails.getModifyPasswordTime();
        if (StringUtils.isBlank(modifyPasswordTime)) {
            return Objects.equals(loginName, userDetails.getLoginName())
                    && hasToken(token);
        }
        return Objects.equals(loginName, userDetails.getLoginName())
                && hasToken(token) && validate(created, LocalDateTimeUtil.parse(modifyPasswordTime, LocalDateTimeUtil.DEFAULT_PATTERN));
    }

    /**
     * 校验token是否合法
     *
     * @param token token的值
     * @return true:合法，false：非法
     */
    public Boolean validate(String token) {
        final String loginName = jwtBuilder.getLoginNameFromToken(token);
        final Instant created = jwtBuilder.getCreatedTimeFromToken(token);
        return hasToken(token);
    }


    private Boolean validate(Instant created, LocalDateTime lastPasswordReset) {
        return lastPasswordReset == null || created.isAfter(Instant.from(lastPasswordReset));
    }

    /**
     * 判断token是否过期
     *
     * @param token token
     * @return true | false
     */
    private Boolean hasToken(String token) {
        return this.valueOperations.getOperations().hasKey(jwtProperties.getPrefix() + Md5Utils.digestMD5(token));
    }
}
