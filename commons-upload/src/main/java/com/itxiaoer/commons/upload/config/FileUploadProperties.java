package com.itxiaoer.commons.upload.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : liuyk
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "commons.upload")
@EnableConfigurationProperties
public class FileUploadProperties {

    /**
     * 文件存放目录
     */
    @Value("${commons.upload.dir}")
    private String dir;

    /**
     * id生成规则
     */
    @Value("${commons.upload.idRule:uuid}")
    private String idRule;

}
