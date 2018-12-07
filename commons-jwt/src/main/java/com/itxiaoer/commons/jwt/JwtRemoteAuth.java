package com.itxiaoer.commons.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : liuyk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("WeakerAccess")
public class JwtRemoteAuth implements Serializable {
    private String loginName;
    private LocalDateTime modifyPasswordTime;
}
