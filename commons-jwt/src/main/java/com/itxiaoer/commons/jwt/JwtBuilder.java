package com.itxiaoer.commons.jwt;

import com.itxiaoer.commons.core.util.Lists;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : liuyk
 */

@Slf4j
@SuppressWarnings({"WeakerAccess", "unused"})
public class JwtBuilder implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;
    /**
     * 主题
     */
    private static final String CLAIM_KEY_LOGIN_NAME = "sub";
    private static final String CLAIM_KEY_MICK_NAME = "nickName";
    private static final String CLAIM_KEY_ID = "id";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_ROLE = "role";
    private static final String CLAIM_KEY_UPDATE_PASSWORD = "update";

    @Resource
    private JwtProperties jwtProperties;

    /**
     * 重token中获取用户登录名称
     *
     * @param token 当前token
     * @return 当前登录用户登录名
     */
    public String getLoginNameFromToken(String token) {
        String username = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            log.error("get username from jwt token error :", e);
        }
        return username;
    }

    /**
     * 重token中获取用户登录名称
     *
     * @param token 当前token
     * @return 当前登录用户登录名
     */
    public String getIdFromToken(String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            return (String) claims.get(CLAIM_KEY_ID);
        } catch (Exception e) {
            log.error("get id from jwt token error :", e);
            return null;
        }
    }


    /**
     * 重token中获取token创建时间
     *
     * @param token 当前token
     * @return token创建时间
     */
    public Instant getCreatedTimeFromToken(String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            return Instant.ofEpochMilli((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            log.error("get created Time from jwt token error :", e);
            return null;
        }
    }

    /**
     * 重token中获取token过期时间
     *
     * @param token 当前token
     * @return token创建时间
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            log.error("get expiration Time from jwt token error :", e);
            return null;
        }
    }

    /**
     * 解析token
     *
     * @param token 当前token
     * @return 解析后的值
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("get claims from jwt token error :", e);
            claims = null;
        }
        return claims;
    }


    /**
     * 生成token过期时间
     *
     * @return token过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + jwtProperties.getExpiration() * 1000);
    }


    /**
     * 通过用户信息创建token
     *
     * @param userDetails 用户信息对象
     * @return token
     */
    public JwtToken build(JwtAuth userDetails) {
        Date expireTime = generateExpirationDate();
        Map<String, Object> claims = new HashMap<>(16);
        claims.put(CLAIM_KEY_LOGIN_NAME, userDetails.getLoginName());
        claims.put(CLAIM_KEY_ID, userDetails.getId());
        claims.put(CLAIM_KEY_MICK_NAME, userDetails.getNickName());
        claims.put(CLAIM_KEY_CREATED, expireTime.getTime());
        claims.put(CLAIM_KEY_ROLE, userDetails.getRoles());
        return new JwtToken(build(claims, expireTime), expireTime.getTime());
    }


    /**
     * 通过map创建token对象
     *
     * @param claims map
     * @return token
     */
    private String build(Map<String, Object> claims, Date expireTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 刷新token的值
     *
     * @param token 原来的toeken值
     * @return 刷新后的token值
     */
    public JwtToken refreshToken(String token) {
        String refreshedToken;
        Date expireTime = generateExpirationDate();
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, expireTime.getTime());
            refreshedToken = build(claims, expireTime);
        } catch (Exception e) {
            log.error("refreshedToken jwt token error :", e);
            refreshedToken = null;
        }
        return new JwtToken(refreshedToken, expireTime.getTime());
    }


    /**
     * 判断token是否过期
     *
     * @param token token
     * @return 是否过期
     */
    public Boolean isExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    /**
     * 校验token是否合法
     *
     * @param token token的值
     * @return true:合法，false：非法
     */
    @SuppressWarnings("all")
    public JwtAuth getJwtAuth(String token) {
        Claims claims = this.getClaimsFromToken(token);
        List<String> roles = (List<String>) claims.get(CLAIM_KEY_ROLE);
        return new JwtAuth((String) claims.get(CLAIM_KEY_ID), (String) claims.get(CLAIM_KEY_LOGIN_NAME), (String) claims.get(CLAIM_KEY_MICK_NAME), Lists.iterable(roles) ? roles : Lists.newArrayList());
    }
}

