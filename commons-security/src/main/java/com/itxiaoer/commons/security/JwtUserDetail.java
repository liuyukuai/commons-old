package com.itxiaoer.commons.security;


import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

/**
 * @author : liuyk
 */
public interface JwtUserDetail extends UserDetails {
    /**
     * 获取密码修改时间
     *
     * @return 密码修改时间
     */
    LocalDateTime getModifyPasswordTime();
}
