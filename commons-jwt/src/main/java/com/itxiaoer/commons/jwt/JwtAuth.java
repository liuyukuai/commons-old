package com.itxiaoer.commons.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * @author : liuyk
 */
@Data
@SuppressWarnings({"unused", "WeakerAccess"})
public class JwtAuth {
    private String id;
    private String loginName;
    private String nickName;
    @JsonIgnore
    private String token;
    @JsonIgnore
    private boolean notExpired;
    private List<String> roles;

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
