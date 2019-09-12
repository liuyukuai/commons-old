package com.itxiaoer.commons.wx;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : liuyk
 */
@Data
@SuppressWarnings("ALL")
public class WxUserListOfTag implements Serializable {
    private static final long serialVersionUID = -1188341114492367222L;

    private String errcode;
    private String errmsg;
    private String tagname;
    private List<WxUser> userlist;

    @Data
    public static class WxUser implements Serializable {
        private static final long serialVersionUID = -272297650625635192L;
        private String userid;
        private String name;
    }
}
