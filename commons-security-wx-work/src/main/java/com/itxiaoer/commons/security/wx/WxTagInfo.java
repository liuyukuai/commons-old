package com.itxiaoer.commons.security.wx;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : liuyk
 */
@Data
@SuppressWarnings("ALL")
public class WxTagInfo implements Serializable {
    private static final long serialVersionUID = -1188341114492367222L;

    private String errcode;
    private String errmsg;
    private List<Tag> taglist;

    @Data
    static class Tag implements Serializable {
        private static final long serialVersionUID = -272297650625635192L;
        private String tagid;
        private String tagname;
    }
}
