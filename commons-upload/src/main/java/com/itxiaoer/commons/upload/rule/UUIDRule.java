package com.itxiaoer.commons.upload.rule;

import com.itxiaoer.commons.core.util.UUIDUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : liuyk
 */

@Component

public class UUIDRule implements IdRule {

    @Override
    public String id(MultipartFile file) {
        return UUIDUtils.guid();
    }
}
