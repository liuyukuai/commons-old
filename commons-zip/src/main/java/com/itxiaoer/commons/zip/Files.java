package com.itxiaoer.commons.zip;

import java.io.File;

/**
 * @author : liuyk
 */
//@Slf4j
@SuppressWarnings("WeakerAccess")
public final class Files {


    public static void mkdirs(String destPath) {
        File dir = new File(destPath);
        mkdirs(dir);
    }


    public static void mkdirs(File dir) {
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (!mkdirs) {
//                log.error("Failed to create folder, path:[{}]", dir.getAbsolutePath());
            }
        }
    }
}
