package com.itxiaoer.commons.security.wx;

import com.itxiaoer.commons.core.date.LocalDateTimeUtil;
import com.itxiaoer.commons.security.JwtUserDetail;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : liuyk
 */
@Setter
@Getter
@SuppressWarnings({"unused", "WeakerAccess"})
public class WxUserInfo extends JwtUserDetail {

    private static final long serialVersionUID = 2073919952024602670L;

    private String modifyPasswordTime;

    private List<String> departments;

    private List<String> tags;

    public WxUserInfo() {
    }

    public WxUserInfo(String id, String loginName, String nickName, List<String> roles) {
        super(id, loginName, nickName, roles);
    }

    public WxUserInfo(String id, String loginName, String nickName, String avatar, List<String> roles) {
        super(id, loginName, nickName, avatar, roles);
    }

    @Override
    public LocalDateTime getModifyPasswordTime() {
        if (StringUtils.isNotBlank(this.modifyPasswordTime)) {
            return LocalDateTimeUtil.parse(this.modifyPasswordTime, LocalDateTimeUtil.DEFAULT_PATTERN);
        }
        return null;
    }
}
