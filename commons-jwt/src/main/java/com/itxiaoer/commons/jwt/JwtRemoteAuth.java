package com.itxiaoer.commons.jwt;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : liuyk
 */
@Data
@SuppressWarnings("WeakerAccess")
public class JwtRemoteAuth implements Serializable {
    private String id;
    private String loginName;
    private String nickName;
    private String modifyPasswordTime;
    private List<String> roles;

    public JwtRemoteAuth() {
    }

    public JwtRemoteAuth(String loginName, String modifyPasswordTime) {
        this.loginName = loginName;
        this.modifyPasswordTime = modifyPasswordTime;
    }
}
