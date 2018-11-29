package com.itxiaoer.commons.jwt;

import lombok.Data;

import java.util.List;

/**
 * @author : liuyk
 */
@Data
@SuppressWarnings({"unused", "WeakerAccess"})
public class JwtAuth {
    private final String id;
    private final String loginName;
    private final String nickName;
    private final List<String> roles;

    public JwtAuth(
            String id,
            String loginName,
            String nickName,
            List<String> roles) {
        this.id = id;
        this.loginName = loginName;
        this.nickName = nickName;
        this.roles = roles;
    }
}
