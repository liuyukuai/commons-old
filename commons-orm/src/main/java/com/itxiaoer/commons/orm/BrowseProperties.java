package com.itxiaoer.commons.orm;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 浏览数记录配置
 *
 * @author : liuyk
 */
@Data
@Configuration
@ConfigurationProperties
public class BrowseProperties {
    /**
     * 记录时间间隔
     */
    @Value("${commons.orm.browse.interval:36000}")
    private Long interval;
}
