package com.itxiaoer.commons.wx;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.itxiaoer.commons.core.json.JsonUtil;
import com.itxiaoer.commons.core.page.Response;
import com.itxiaoer.commons.core.util.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 通讯录管理
 *
 * @author : liuyk
 */
@Slf4j
@SuppressWarnings("ALL")
public class WxAddressService {

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
                ResponseEntity<String> response = restTemplate.getForEntity(String.format(WxConstants.WX_TOKEN_URL, wxProperties.getAppId(), wxProperties.getAddressSecret()), String.class);
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

    private LoadingCache<String, WxUser> userInfoCache
            = Caffeine.newBuilder()
            //设置写缓存后1个小时过期
            .expireAfterWrite(30, TimeUnit.MINUTES)
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
                    ResponseEntity<WxUser> response = restTemplate.getForEntity(String.format(WxConstants.WX_USER_QUERY_URL, getToken(), key), WxUser.class);
                    if (log.isDebugEnabled()) {
                        log.debug("load wx user response = {} ", response);
                    }

                    WxUser wxUser = response.getBody();

                    if (!Objects.isNull(wxUser)) {

                        Set<String> tagsList = this.getTagsById(wxUser.getUserid());
                        wxUser.setTags(tagsList);
                    }
                    return wxUser;
                }
                throw new RuntimeException("load user  error, the key  is null. ");
            });


    private LoadingCache<String, Map<String, Set<String>>> userTagMapCache
            = Caffeine.newBuilder()
            //设置写缓存后1个小时过期
            .expireAfterWrite(30, TimeUnit.MINUTES)
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

    public WxUser getUserById(String userId) {
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


    @SuppressWarnings("unused")
    public Response<String> uploadAvatar(File file) throws IOException {
        if (!Objects.isNull(file)) {
            //先上传图片
            FileSystemResource resource = new FileSystemResource(file);
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            param.add("file", resource);
            param.add("Content-Type", "multipart/form-data");
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param);
            ResponseEntity<String> responseEntity = restTemplate.exchange(String.format(WxConstants.WX_USER_UPLOAD_MEDIA_URL, getToken(), "image"), HttpMethod.POST, httpEntity, String.class);
            if (log.isDebugEnabled()) {
                log.debug(" upload wx avatar response = [{}]", responseEntity.getBody());
            }
            String body = responseEntity.getBody();
            Map jsonObject = JsonUtil.toBean(body, Map.class).orElseGet(() -> {
                log.warn("upload wx avatar response = [{}]", responseEntity);
                return Collections.emptyMap();
            });
            String mediaId = (String) jsonObject.get("media_id");
            return Response.ok(mediaId);
        }
        return Response.failure("文件为空");
    }


    /**
     * 创建微信用户
     *
     * @param wxCreateUser wxCreateUser
     * @return Response
     */
    @SuppressWarnings("ALL")
    public WxResponse createUser(WxUser wxCreateUser) {
        ResponseEntity<WxResponse> response = restTemplate.postForEntity(String.format(WxConstants.WX_USER_CREATE_URL, getToken()), wxCreateUser, WxResponse.class);
        if (log.isDebugEnabled()) {
            log.debug(" create wx user response = [{}]", response.getBody());
        }
        return response.getBody();
    }

    /**
     * 给用户添加tag
     *
     * @param tagId   tagId
     * @param userIds userIds
     * @return WxResponse
     */
    @SuppressWarnings("ALL")
    public WxResponse createUserTag(String tagId, List<String> userIds) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("tagid", tagId);
        params.put("userlist", userIds);
        // 创建tag
        ResponseEntity<WxResponse> response = restTemplate.postForEntity(String.format(WxConstants.WX_USER_CREATE_TAG_URL, getToken()), params, WxResponse.class);
        if (log.isDebugEnabled()) {
            log.debug(" create wx user tag response = [{}]", response.getBody());
        }
        return response.getBody();
    }


    /**
     * 给用户添加tag
     *
     * @param tagId   tagId
     * @param userIds userIds
     * @return WxResponse
     */
    @SuppressWarnings("ALL")
    public WxResponse deleteUserTag(String tagId, List<String> userIds) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("tagid", tagId);
        params.put("userlist", userIds);
        // 创建tag
        ResponseEntity<WxResponse> response = restTemplate.postForEntity(String.format(WxConstants.WX_USER_DELETE_TAG_URL, getToken()), params, WxResponse.class);
        if (log.isDebugEnabled()) {
            log.debug(" create wx user tag response = [{}]", response.getBody());
        }
        return response.getBody();
    }


    /**
     * 创建微信用户
     *
     * @param wxCreateUser wxCreateUser
     * @return Response
     */
    @SuppressWarnings("ALL")
    public WxResponse updateUser(WxUser wxCreateUser) {
        ResponseEntity<WxResponse> response = restTemplate.postForEntity(String.format(WxConstants.WX_USER_UPDATE_URL, getToken()), wxCreateUser, WxResponse.class);
        if (log.isDebugEnabled()) {
            log.debug(" create wx user response = [{}]", response.getBody());
        }
        return response.getBody();
    }

    /**
     * 通过id删除用户
     *
     * @param id id
     * @return WxResponse
     */
    public WxResponse deleteUserById(String id) {
        ResponseEntity<WxResponse> response = restTemplate.getForEntity(String.format(WxConstants.WX_USER_DELETE_URL, getToken(), id), WxResponse.class);
        if (log.isDebugEnabled()) {
            log.debug(" create wx user response = [{}]", response.getBody());
        }
        return response.getBody();
    }

    public void cleanUp() {
        this.tokenCache.cleanUp();
    }

}
