package com.itxiaoer.commons.lbs.baidu;

import com.itxiaoer.commons.lbs.LbsService;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : liuyk
 */
@Data
@Configuration
@ConfigurationProperties("spring.lbs.baidu")
@ConditionalOnProperty("spring.lbs.baidu.ak")
public class BaiduConfig {
    private String ak;

    @Bean
    public LbsService lbsService() {
        return new BaiduLibsServiceImpl();
    }
}
