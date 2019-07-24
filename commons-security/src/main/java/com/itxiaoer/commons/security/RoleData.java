package com.itxiaoer.commons.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色对象
 *
 * @author : liuyk
 * 
 */
@SuppressWarnings("WeakerAccess")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleData {
    private String url;
    private String method;
}
