package com.itxiaoer.commons.security.wx;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信创建用户信息
 *
 * @author : liuyk
 */
@Data
public class WxCreateUser implements Serializable {
    private static final long serialVersionUID = -9041682557493738935L;
    private String userid;
    private String name;
    private String alias;
    private String mobile;
    private Long[] department;
    private Long[] order;
    private String position;
    private String gender;
    private String email;
    private Long[] is_leader_in_dept;
    private Long enable = 1L;
    private String avatar_mediaid;
    private String telephone;
    private String address;
    private String tagid;

}
