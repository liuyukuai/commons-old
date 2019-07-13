package com.itxiaoer.commons.upload.rule;

import com.itxiaoer.commons.core.util.Md5Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : liuyk
 */

public class Md5IdRule implements IdRule {

    @Override
    public String id(MultipartFile file) {
        try {
            return Md5Utils.digestMD5(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
