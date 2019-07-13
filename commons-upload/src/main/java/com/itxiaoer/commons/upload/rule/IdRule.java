package com.itxiaoer.commons.upload.rule;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author : liuyk
 */

public interface IdRule {

    String id(MultipartFile file);
}
