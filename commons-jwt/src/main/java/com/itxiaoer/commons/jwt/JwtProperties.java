package com.itxiaoer.commons.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : liuyk
 */
@Data
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {
    private String header;

    private String secret;

    private Long expiration;
}
