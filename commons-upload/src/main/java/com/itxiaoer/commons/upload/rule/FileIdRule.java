package com.itxiaoer.commons.upload.rule;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author : liuyk
 */

public class FileIdRule implements IdRule {

    @Override
    public String id(MultipartFile file) {
        return file.getOriginalFilename();
    }
}
