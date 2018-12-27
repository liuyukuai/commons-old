package com.itxiaoer.commons.security;


import com.itxiaoer.commons.jwt.JwtAuth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : liuyk
 */
@SuppressWarnings("all")
public abstract class JwtUserDetail extends JwtAuth implements UserDetails {
    public JwtUserDetail() {
    }

    public JwtUserDetail(String id, String loginName, String nickName, List<String> roles) {
        super(id, loginName, nickName, roles);
    }

    public JwtUserDetail(String id, String loginName, String nickName, String avatar, List<String> roles) {
        super(id, loginName, nickName, avatar, roles);
    }

    /**
     * 获取密码修改时间
     *
     * @return 密码修改时间
     */
    public abstract LocalDateTime getModifyPasswordTime();

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.getLoginName();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
