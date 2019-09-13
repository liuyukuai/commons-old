package com.itxiaoer.commons.security.wx;

import com.itxiaoer.commons.core.util.Lists;
import com.itxiaoer.commons.security.JwtUserDetail;
import com.itxiaoer.commons.security.JwtUserDetailService;
import com.itxiaoer.commons.wx.WxAddressService;
import com.itxiaoer.commons.wx.WxLoginService;
import com.itxiaoer.commons.wx.WxProperties;
import com.itxiaoer.commons.wx.WxUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : liuyk
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public class WxUseDetailServiceImpl implements JwtUserDetailService, InitializingBean {

    @Resource
    @Qualifier("wxRestTemplate")
    private RestTemplate restTemplate;

    @Resource
    private WxProperties wxProperties;

    @Resource
    private WxAddressService wxAddressService;

    @Resource
    private WxLoginService wxLoginService;




    @SuppressWarnings("all")
    private WxUserInfo getUserById(Map jsonObject) {
        try {
            WxUserInfo userInfo = new WxUserInfo();
            List<Object> departments = (List<Object>) jsonObject.get("department");

            List<String> strings = Optional.ofNullable(departments).map((e) ->
                    e.stream().map(Object::toString).collect(Collectors.toList())).orElse(Lists.newArrayList());
            userInfo.setDepartments(strings);
            userInfo.setAvatar(jsonObject.get("avatar").toString());
            userInfo.setLoginName(jsonObject.get("userid").toString());
            userInfo.setId(userInfo.getLoginName());
            userInfo.setNickName(jsonObject.get("name").toString());
            userInfo.setRoles(Collections.singletonList("ROLE_USER"));
            return userInfo;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }


    @Override
    public JwtUserDetail loadUserByUsername(String code) throws UsernameNotFoundException {
        WxUser userByCode = wxLoginService.getUserByCode(code);
        return getUserById(userByCode);
    }

    @Override
    public JwtUserDetail loadUserByUsername(String userId, String token) throws UsernameNotFoundException {
        return this.getUserById(userId);
    }


    public WxUserInfo getUserById(String userId) {
        WxUser wxUser = wxAddressService.getUserById(userId);
        return getUserById(wxUser);
    }


    public WxUserInfo getUserById(WxUser wxUser) {
        if (Objects.nonNull(wxUser)) {
            WxUserInfo userInfo = new WxUserInfo();
            List<Integer> departments = wxUser.getDepartment();
            List<String> strings = Optional.ofNullable(departments).map((e) ->
                    e.stream().map(Object::toString).collect(Collectors.toList())).orElse(Lists.newArrayList());
            userInfo.setDepartments(strings);
            userInfo.setAvatar(wxUser.getAvatar());

            userInfo.setLoginName(wxUser.getUserid());
            userInfo.setId(userInfo.getLoginName());
            userInfo.setNickName(wxUser.getName());
            userInfo.setRoles(Collections.singletonList("ROLE_USER"));
            userInfo.setTags(wxUser.getTags());
            return userInfo;
        }
        return null;
    }


    @Override
    public void afterPropertiesSet() {
        wxAddressService.getTagsById("1");
    }
}
