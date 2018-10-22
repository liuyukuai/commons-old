package com.itxiaoer.commons.orm.service;

import com.itxiaoer.commons.core.ParameterException;
import com.itxiaoer.commons.orm.BrowseProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.function.Function;

/**
 * 浏览数记录接口
 *
 * @author : liuyk
 */
@Slf4j
@SuppressWarnings("all")
public abstract class BaseBrowseService {

    @Autowired
    private BrowseProperties browseProperties;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 记录数据的浏览量
     *
     * @param id       要记录数据的id
     * @param token    用户唯一标识
     * @param function 记录浏览数的真正方法
     */
    public void browse(String id, String token, Function<String, Boolean> function) {
        // 判断toke值
        if (StringUtils.isBlank(token)) {
            throw new ParameterException(" token is null. ");
        }
        if (log.isDebugEnabled()) {
            log.debug("get token = " + token);

        }

        String key = this.key(id, token);
        String isRecord = redisTemplate.opsForValue().get(key);
        // 如果已经记录，结束
        if (StringUtils.isNotBlank(isRecord)) {
            return;
        }

        // 处理并发的问题
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "1");
        if (result != null && result) {
            try {
                boolean handle = function.apply(id);
                if (handle) {
                    // 过期时间
                    Instant now = Instant.now().plus(browseProperties.getInterval() * 1000 + new Random().nextInt(1000), ChronoUnit.MILLIS);
                    redisTemplate.expireAt(key, Date.from(now));
                    return;
                }
                redisTemplate.delete(key);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                redisTemplate.delete(key);
            }
        }
    }


    public String key(String id, String token) {
        return token + id;
    }

}
