package com.itxiaoer.commons.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : liuyk
 */
@Data
@SuppressWarnings({"unused", "WeakerAccess"})
public class JwtAuth implements Serializable {
    private static final long serialVersionUID = -6764469906496100993L;
    private String id;
    private String loginName;
    private String nickName;
    private String avatar;
    @JsonIgnore
    private String token;
    private boolean notExpired;
    private List<String> roles;

    public JwtAuth() {
        this.notExpired = true;
    }


    public JwtAuth(
            String id,
            String loginName,
            String nickName,
            List<String> roles) {
        this.id = id;
        this.loginName = loginName;
        this.nickName = nickName;
        this.roles = roles;
        this.notExpired = true;
    }

    public JwtAuth(
            String id,
            String loginName,
            String nickName,
            String avatar,
            List<String> roles) {
        this.id = id;
        this.loginName = loginName;
        this.nickName = nickName;
        this.roles = roles;
        this.avatar = avatar;
        this.notExpired = true;
    }
}
