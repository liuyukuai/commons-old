package com.itxiaoer.commons.upload.config;

import com.itxiaoer.commons.upload.FileUploadController;
import com.itxiaoer.commons.upload.rule.FileIdRule;
import com.itxiaoer.commons.upload.rule.IdRule;
import com.itxiaoer.commons.upload.rule.Md5IdRule;
import com.itxiaoer.commons.upload.rule.UUIDRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传配置
 *
 * @author : liuyk
 */
@Configuration
public class FileUploadConfig {

    @Bean
    public FileUploadProperties fileUploadProperties() {
        return new FileUploadProperties();
    }


    @Bean
    @ConditionalOnProperty(value = "commons.upload.id-rule", havingValue = "md5")
    public IdRule md5IdRule() {
        return new Md5IdRule();
    }

    @Bean
    @ConditionalOnProperty(value = "commons.upload.id-rule", havingValue = "uuid")
    public IdRule uuidIdRule() {
        return new UUIDRule();
    }

    @Bean
    @ConditionalOnProperty(value = "commons.upload.id-rule", havingValue = "file")
    public IdRule fileIdRule() {
        return new FileIdRule();
    }

    @Bean
    @ConditionalOnMissingBean(FileUploadController.class)
    public FileUploadController fileUploadController() {
        return new FileUploadController();
    }

}
