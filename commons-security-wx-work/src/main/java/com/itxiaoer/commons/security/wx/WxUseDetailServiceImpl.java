package com.itxiaoer.commons.security.wx;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.itxiaoer.commons.core.json.JsonUtil;
import com.itxiaoer.commons.core.util.Lists;
import com.itxiaoer.commons.security.JwtUserDetail;
import com.itxiaoer.commons.security.JwtUserDetailService;
import com.itxiaoer.commons.wx.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
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


    private LoadingCache<String, String> tokenCache
            = Caffeine.newBuilder()
            //设置写缓存后1个小时过期
            .expireAfterWrite(1, TimeUnit.HOURS)
            //设置缓存容器的初始容量为10
            .initialCapacity(1)
            //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
            .maximumSize(10)
            //设置要统计缓存的命中率
            .recordStats()
            //设置缓存的移除通知
            .removalListener((k, v, cause) ->
                    log.info(k + " {} = {}  was removed, cause is {} ", k, v, cause)
            )
            .build((key) -> {
                ResponseEntity<String> response = restTemplate.getForEntity(String.format(WxConstants.WX_TOKEN_URL, wxProperties.getAppId(), wxProperties.getSecret()), String.class);
                if (log.isDebugEnabled()) {
                    log.debug("oad wx token response = {} ", response);
                }

                String body = response.getBody();
                if (StringUtils.isNotBlank(body)) {
                    return JsonUtil.toBean(body, Token.class).map(Token::getAccess_token).orElseGet(() -> {
                        log.warn("load wx token  error , {} ", response);
                        return "";
                    });
                }
                log.error("load wx token  error , {} ", response);
                throw new RuntimeException("load wx token error ");
            });

    public String getToken() {
        try {
            return tokenCache.get("token");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

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
        if (log.isDebugEnabled()) {
            log.debug("load user by code , code = {} ", code);
        }
        if (StringUtils.isNotBlank(code)) {

            ResponseEntity<String> response = restTemplate.getForEntity(String.format(WxConstants.WX_USER_BY_CODE_URL, getToken(), code), String.class);
            if (log.isDebugEnabled()) {
                log.debug("load wx user response = {} ", response);
            }

            String userJson = response.getBody();
            Optional<Map> optional = JsonUtil.toBean(userJson, Map.class);

            if (optional.isPresent()) {
                Map map = optional.get();

                String userId = (String) map.get("UserId");

                if (StringUtils.isNotBlank(userId)) {
                    return this.getUserById(userId);
                }
                // 如果用户加载失败
                Integer errCode = (Integer) map.get("errcode");

                // 如果是token过期
                if (Objects.equals(errCode, 42001)) {
                    this.tokenCache.cleanUp();
                }
            }
        }
        return null;
    }

    @Override
    public JwtUserDetail loadUserByUsername(String userId, String token) throws UsernameNotFoundException {
        return this.getUserById(userId);
    }


    public WxUserInfo getUserById(String userId) {
        WxUser wxUser = wxAddressService.getUserById(userId);
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
