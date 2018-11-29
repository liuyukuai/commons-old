package com.itxiaoer.commons.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author : liuyk
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JwtAuth implements UserDetails {
    private final String id;
    private final String nickName;
    private final String password;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final LocalDateTime lastUpdatePasswordTime;

    public JwtAuth(
            String id,
            String nickName,
            String password,
            String email,
            Collection<? extends GrantedAuthority> authorities,
            LocalDateTime lastUpdatePasswordTime) {
        this.id = id;
        this.nickName = nickName;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.lastUpdatePasswordTime = lastUpdatePasswordTime;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    public LocalDateTime getLastPasswordResetTime() {
        return lastUpdatePasswordTime;
    }
}
