package com.itxiaoer.commons.security.wx;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.itxiaoer.commons.core.json.JsonUtil;
import com.itxiaoer.commons.core.page.Response;
import com.itxiaoer.commons.core.util.Lists;
import com.itxiaoer.commons.security.JwtUserDetail;
import com.itxiaoer.commons.security.JwtUserDetailService;
import com.itxiaoer.commons.security.wx.commons.Token;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
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


    private LoadingCache<String, WxUserInfo> userInfoCache
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
                if (StringUtils.isNotBlank(key)) {
                    ResponseEntity<String> response = restTemplate.getForEntity(String.format(WxConstants.WX_USER_ID_URL, getToken(), key), String.class);
                    if (log.isDebugEnabled()) {
                        log.debug("load wx user response = {} ", response);
                    }

                    String body = response.getBody();
                    if (StringUtils.isNotBlank(body)) {
                        Map jsonObject = JsonUtil.toBean(body, Map.class).orElseGet(() -> {
                            log.warn("load wx user error , {} ", response);
                            return Collections.emptyMap();
                        });
                        WxUserInfo process = process(jsonObject);
                        if (!Objects.isNull(process)) {

                            Set<String> tagsList = this.getTagsById(process.getId());
                            process.setTags(tagsList);
                        }
                        return process;
                    }
                    log.error("load wx user error ,{} ", response);
                }
                throw new RuntimeException("load user  error, response is null. ");
            });


    private LoadingCache<String, Map<String, Set<String>>> userTagMapCache
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
            .build((key) -> this.userTagMap());


    public String getToken() {
        try {
            return tokenCache.get("token");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public WxUserInfo getUserById(String userId) {
        try {
            return userInfoCache.get(userId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public Set<String> getTagsById(String userId) {
        try {
            Map<String, Set<String>> stringSetMap = userTagMapCache.get("tagMap");
            return stringSetMap != null ? stringSetMap.get(userId) : Collections.emptySet();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptySet();
        }
    }

    /**
     * 获取用户和tag的关系列表
     *
     * @return map
     */
    private Map<String, Set<String>> userTagMap() {
        Map<String, Set<String>> userTagMap = new HashMap<>(16);

        List<String> tags = this.getTags();
        for (String tag : tags) {
            ResponseEntity<String> tagResponse = restTemplate.getForEntity(String.format(WxConstants.WX_USER_OF_TAG_URL, getToken(), tag), String.class);
            if (log.isDebugEnabled()) {
                log.debug("load wx tags userList response = {} ", tagResponse);
            }
            WxUserListOfTag tagMap = JsonUtil.toBean(tagResponse.getBody(), WxUserListOfTag.class).orElseGet(() -> {
                log.warn("load wx user error , {} ", tagResponse);
                return new WxUserListOfTag();
            });

            List<WxUserListOfTag.WxUser> userList = tagMap.getUserlist();
            if (Lists.iterable(userList)) {
                List<String> ids = userList.stream().map(WxUserListOfTag.WxUser::getUserid).collect(Collectors.toList());
                for (String id : ids) {
                    Set<String> strings = userTagMap.get(id);
                    if (strings == null) {
                        strings = new HashSet<>();
                    }
                    strings.add(tag);
                    userTagMap.put(id, strings);
                }
            }
        }
        return userTagMap;
    }


    @SuppressWarnings("all")
    private List<String> getTags() {
        try {
            // 获取tag属性
            ResponseEntity<String> tagResponse = restTemplate.getForEntity(String.format(WxConstants.WX_USER_TAG_URL, getToken()), String.class);
            if (log.isDebugEnabled()) {
                log.debug("load wx user tags response = {} ", tagResponse);
            }

            WxTagInfo tagMap = JsonUtil.toBean(tagResponse.getBody(), WxTagInfo.class).orElseGet(() -> {
                log.warn("load wx user error , {} ", tagResponse);
                return new WxTagInfo();
            });
            List<WxTagInfo.Tag> tagList = tagMap.getTaglist();

            if (Lists.iterable(tagList)) {
                return tagList.stream().map(WxTagInfo.Tag::getTagid).collect(Collectors.toList());
            }
            return Collections.emptyList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }


    @SuppressWarnings("all")
    private WxUserInfo process(Map jsonObject) {
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
                    return getUserById(userId);
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


    /**
     * 创建微信用户
     *
     * @param wxCreateUser wxCreateUser
     * @param file         file
     * @return Response
     */
    @SuppressWarnings("ALL")
    public Response<String> createUser(WxCreateUser wxCreateUser, File file) {
        if (!Objects.isNull(file)) {
            //先上传图片
            FileSystemResource resource = new FileSystemResource(file);
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            param.add("file", resource);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param);
            ResponseEntity<String> responseEntity = restTemplate.exchange(String.format(WxConstants.WX_USER_UPLOAD_MEDIA_URL, getToken()), HttpMethod.POST, httpEntity, String.class);
            if (log.isDebugEnabled()) {
                log.debug(" upload wx avatar response = [{}]", responseEntity.getBody());
            }
            String body = responseEntity.getBody();
            Map jsonObject = JsonUtil.toBean(body, Map.class).orElseGet(() -> {
                log.warn("upload wx avatar response = [{}]", responseEntity);
                return Collections.emptyMap();
            });

            String mediaId = (String) jsonObject.get("media_id");
            if (StringUtils.isNotBlank(mediaId)) {
                wxCreateUser.setAvatar_mediaid(mediaId);
            }
        }
        ResponseEntity<String> response = restTemplate.postForEntity(String.format(WxConstants.WX_USER_CREATE_URL, getToken()), wxCreateUser, String.class);
        if (log.isDebugEnabled()) {
            log.debug(" create wx user response = [{}]", response.getBody());
        }
        Map<String, Object> params = new HashMap<>();
        params.put("tagid", wxCreateUser.getTagid());
        params.put("userlist", Collections.singletonList(wxCreateUser.getUserid()));
        // 创建tag
        response = restTemplate.postForEntity(String.format(WxConstants.WX_USER_CREATE_TAG_URL, getToken()), params, String.class);
        if (log.isDebugEnabled()) {
            log.debug(" create wx user tag response = [{}]", response.getBody());
        }
        return Response.ok();
    }


    /**
     * 创建微信用户
     *
     * @param wxCreateUser wxCreateUser
     * @param file         file
     * @return Response
     */
    @SuppressWarnings("ALL")
    public Response<String> updateUser(WxCreateUser wxCreateUser, File file) {
        if (!Objects.isNull(file)) {
            //先上传图片
            FileSystemResource resource = new FileSystemResource(file);
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            param.add("file", resource);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param);
            ResponseEntity<String> responseEntity = restTemplate.exchange(String.format(WxConstants.WX_USER_UPLOAD_MEDIA_URL, getToken()), HttpMethod.POST, httpEntity, String.class);
            if (log.isDebugEnabled()) {
                log.debug(" upload wx avatar response = [{}]", responseEntity.getBody());
            }
            String body = responseEntity.getBody();
            Map jsonObject = JsonUtil.toBean(body, Map.class).orElseGet(() -> {
                log.warn("upload wx avatar response = [{}]", responseEntity);
                return Collections.emptyMap();
            });

            String mediaId = (String) jsonObject.get("media_id");
            if (StringUtils.isNotBlank(mediaId)) {
                wxCreateUser.setAvatar_mediaid(mediaId);
            }
        }
        ResponseEntity<String> response = restTemplate.postForEntity(String.format(WxConstants.WX_USER_UPDATE_URL, getToken()), wxCreateUser, String.class);
        if (log.isDebugEnabled()) {
            log.debug(" create wx user response = [{}]", response.getBody());
        }
        return Response.ok();
    }


    @Override
    public void afterPropertiesSet() {
        this.getTagsById("1");
    }
}
