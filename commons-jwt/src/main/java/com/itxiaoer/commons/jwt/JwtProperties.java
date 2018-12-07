package com.itxiaoer.commons.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author : liuyk
 */
@Data
public class JwtProperties {

    @Value("${spring.jwt.header:Authorization}")
    private String header;

    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.jwt.expiration:7200}")
    private long expiration;

    @Value("${spring.jwt.store.prefix:redis_token_prefix}")
    private String prefix;
}
