package com.itxiaoer.commons.jwt;

import com.itxiaoer.commons.core.json.JsonUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

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
    private static final String CLAIM_KEY_CREATED = "created";

    @Resource
    private JwtProperties jwtProperties;


    /**
     * 重token中获取token创建时间
     *
     * @param token 当前token
     * @return token创建时间
     */
    public Instant createdTime(String token) {
        final Claims claims = claims(token);
        return Instant.ofEpochMilli((Long) claims.get(CLAIM_KEY_CREATED));
    }

    /**
     * 重token中获取token过期时间
     *
     * @param token 当前token
     * @return token创建时间
     */
    public Date expirationDate(String token) {
        final Claims claims = claims(token);
        return claims.getExpiration();
    }

    /**
     * 解析token
     *
     * @param token 当前token
     * @return 解析后的值
     */
    private Claims claims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("get claims from jwt token error : {}", e.getMessage());
            throw e;
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
     * @param t 需要encode的对象
     * @return token
     */
    public <T> JwtToken encode(T t) {
        Date expireTime = generateExpirationDate();
        Map<String, Object> claims = JsonUtil.toMap(t).orElseThrow(() -> new RuntimeException("parse jwt object error."));
        return new JwtToken(encode(claims, expireTime), expireTime.getTime());
    }


    /**
     * 通过map创建token对象
     *
     * @param claims map
     * @return token
     */
    private String encode(Map<String, Object> claims, Date expireTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    public String get(String token, String name) {
        return Optional.ofNullable(this.claims(token).get(name)).map(String::valueOf).orElse("");
    }

    /**
     * 刷新token的值
     *
     * @param token 原来的token值
     * @return 刷新后的token值
     */
    public JwtToken refresh(String token) {
        String refreshedToken;
        Date expireTime = generateExpirationDate();
        final Claims claims = claims(token);
        claims.put(CLAIM_KEY_CREATED, expireTime.getTime());
        refreshedToken = encode(claims, expireTime);
        return new JwtToken(refreshedToken, expireTime.getTime(), expireTime.getTime());
    }


    /**
     * 判断token是否过期
     *
     * @param token token
     * @return 是否过期
     */
    public Boolean isExpired(String token) {
        final Date expiration = expirationDate(token);
        return Optional.ofNullable(expiration).map(expiration::before).orElse(false);
    }


    /**
     * 获取token中的对象
     *
     * @param token token的值
     * @return obj
     */
    @SuppressWarnings("all")
    public <T> Optional<T> decode(String token, Class<T> clz) {
        Claims claims = this.claims(token);
        return JsonUtil.toBean(claims, clz);
    }
}

