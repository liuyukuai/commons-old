package com.itxiaoer.commons.snowflake;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * id生成器配置
 *
 * @author : liuyk
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "commons.snowflake")
public class IdWorkerProperties {
    /**
     * 当前机器编号，默认0-31
     */
    @Value("${commons.snowflake.workerId:0}")
    private long workerId;

    /**
     * 当前数据中心编号,默认0-31
     */
    @Value("${commons.snowflake.dataCenterId:0}")
    private long dataCenterId;

}
