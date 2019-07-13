package com.itxiaoer.commons.sample.web.service;

import com.itxiaoer.commons.security.JwtUserDetail;
import com.itxiaoer.commons.security.JwtUserDetailService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author : liuyk
 */
@Component
public class UserSampleService implements JwtUserDetailService {
    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public JwtUserDetail loadUserByUsername(String loginName) throws UsernameNotFoundException {
        JwtUserDetail jwtUserDetail = new JwtUserDetail() {

            private static final long serialVersionUID = 7569314337265473599L;

            @Override
            public LocalDateTime getModifyPasswordTime() {
                return null;
            }

            @Override
            public String getPassword() {
                return passwordEncoder.encode("123456");
            }

            @Override
            public String getUsername() {
                return loginName;
            }
        };

        jwtUserDetail.setRoles(Arrays.asList("ROLE_ADMIN"));
        return jwtUserDetail;
    }

}
