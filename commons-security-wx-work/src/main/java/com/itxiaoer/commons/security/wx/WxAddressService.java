package com.itxiaoer.commons.security.wx;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.itxiaoer.commons.core.json.JsonUtil;
import com.itxiaoer.commons.core.page.Response;
import com.itxiaoer.commons.security.wx.commons.Token;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 通讯录管理
 *
 * @author : liuyk
 */
@Slf4j
@SuppressWarnings("WeakerAccess")
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


    public String getToken() {
        try {
            return tokenCache.get("token");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
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
    public Response<String> createUser(WxCreateUser wxCreateUser) {
        ResponseEntity<String> response = restTemplate.postForEntity(String.format(WxConstants.WX_USER_CREATE_URL, getToken()), wxCreateUser, String.class);
        if (log.isDebugEnabled()) {
            log.debug(" create wx user response = [{}]", response.getBody());
        }
        Map<String, Object> params = new HashMap<>(20);
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
     * @return Response
     */
    @SuppressWarnings("ALL")
    public Response<String> updateUser(WxCreateUser wxCreateUser) {
        ResponseEntity<String> response = restTemplate.postForEntity(String.format(WxConstants.WX_USER_UPDATE_URL, getToken()), wxCreateUser, String.class);
        if (log.isDebugEnabled()) {
            log.debug(" create wx user response = [{}]", response.getBody());
        }
        return Response.ok();
    }

    /**
     * 通过id查询用户
     *
     * @param id id
     * @return user
     */
    public Response<WxCreateUser> getUserById(String id) {
        ResponseEntity<WxCreateUser> response = restTemplate.getForEntity(String.format(WxConstants.WX_USER_QUERY_URL, getToken(), id), WxCreateUser.class);
        if (log.isDebugEnabled()) {
            log.debug(" create wx user response = [{}]", response.getBody());
        }
        return Response.ok(response.getBody());
    }

    /**
     * 通过id删除用户
     *
     * @param id id
     * @return
     */
    public Response<String> deleteUserById(String id) {
        ResponseEntity<String> response = restTemplate.getForEntity(String.format(WxConstants.WX_USER_DELETE_URL, getToken(), id), String.class);
        if (log.isDebugEnabled()) {
            log.debug(" create wx user response = [{}]", response.getBody());
        }
        return Response.ok(response.getBody());
    }


}
