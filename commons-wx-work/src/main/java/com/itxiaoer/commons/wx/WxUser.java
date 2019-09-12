package com.itxiaoer.commons.wx;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 微信用户信息
 *
 * @author : liuyk
 */
@Data
@SuppressWarnings("All")
public class WxUser {

    private int errcode;
    private String errmsg;
    private String userid;
    private String name;
    private String position;
    private String mobile;
    private String gender;
    private String email;
    private String avatar;
    private String telephone;
    private int enable;
    private String alias;
    private String address;
    private int status;
    private String qr_code;
    private String external_position;
    private List<Integer> department;
    private List<Integer> order;
    private List<Integer> is_leader_in_dept;
    private Set<String> tags;
    private String tagid;


}
