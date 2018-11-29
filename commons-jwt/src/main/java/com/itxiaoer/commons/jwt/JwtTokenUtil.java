package com.itxiaoer.commons.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : liuyk
 */

@Slf4j
@SuppressWarnings({"WeakerAccess", "unused"})
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_MICK_NAME = "nickName";
    private static final String CLAIM_KEY_CREATED = "created";

    @Resource
    private JwtProperties jwtProperties;

    /**
     * 重token中获取用户登录名称
     *
     * @param token 当前token
     * @return 当前登录用户登录名
     */
    public String getUsernameFromToken(String token) {
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
     * 重token中获取token创建时间
     *
     * @param token 当前token
     * @return token创建时间
     */
    public LocalDateTime getCreatedDateFromToken(String token) {
        LocalDateTime created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = (LocalDateTime) claims.get(CLAIM_KEY_CREATED);
        } catch (Exception e) {
            created = null;
        }
        return created;
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
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
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
     * 判断token是否过期
     *
     * @param token token
     * @return true | false
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(LocalDateTime created, LocalDateTime lastPasswordReset) {
        return (lastPasswordReset != null && created.isBefore(lastPasswordReset));
    }


    /**
     * 通过用户信息创建token
     *
     * @param userDetails 用户信息对象
     * @return token
     */
    public String generateToken(JwtAuth userDetails) {
        Map<String, Object> claims = new HashMap<>(16);
        claims.put(CLAIM_KEY_MICK_NAME, userDetails.getNickName());
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, LocalDateTime.now());
        return generateToken(claims);
    }


    /**
     * 通过map创建token对象
     *
     * @param claims map
     * @return toeken
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, LocalDateTime lastPasswordReset) {
        final LocalDateTime created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }


    /**
     * 刷新token的值
     *
     * @param token 原来的toeken值
     * @return 刷新后的token值
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }


    /**
     * 校验token是否合法
     *
     * @param token       token的值
     * @param userDetails 用户信息
     * @return true:合法，false：非法
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtAuth user = (JwtAuth) userDetails;
        final String username = getUsernameFromToken(token);
        final LocalDateTime created = getCreatedDateFromToken(token);
        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token)
                        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetTime()));
    }
}

