package com.itxiaoer.commons.zip.rar;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.itxiaoer.commons.zip.Files;
import com.itxiaoer.commons.zip.Unpack;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * rar 解压
 *
 * @author : liuyk
 */
@Slf4j
public class RarUnpack implements Unpack {

    @Override
    public void unpack(File file, String destPath, String encoding) {
        Files.mkdirs(destPath);
        try (Archive archive = new Archive(file, new RarUnpackMonitor())) {
            if (archive.isEncrypted()) {
                log.error("Unzip failed and the file was encrypted, file:[{}]", file.getName());
                return;
            }

            List<FileHeader> files = archive.getFileHeaders();

            files.stream()
                    .filter(e -> {
                        if (e.isEncrypted()) {
                            log.warn("Unzip failed and the file was encrypted, file:[{}]", e.getFileNameW());
                            return false;
                        }
                        return true;
                    }).forEach(e -> {
                String fileName = e.getFileNameW();
                File dest = new File(destPath, fileName);
                if (!dest.exists()) {
                    try {
                        boolean newFile = dest.createNewFile();
                        if (!newFile) {
                            log.warn("Failed to create file, file:[{}]", dest.getAbsolutePath());
                            return;
                        }
                    } catch (IOException e1) {
                        log.warn("Failed to create file, file:[{}], error", dest.getAbsolutePath(), e1.getMessage());
                    }

                    try (FileOutputStream fos = new FileOutputStream(dest)) {
                        archive.extractFile(e, fos);
                    } catch (Exception ex) {
                        log.error("Unzip failed , file:[{}] , message:[{}]", e.getFileNameW(), ex.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            log.error("Unzip failed, message:[{}]", e.getMessage());
        }
    }


    public static void main(String[] args) {

        new RarUnpack().unpack(new File("/Users/liuyukuai/Documents/job/运动员信息采集/9障碍追逐队.rar"), "/Users/liuyukuai/Documents/job/files", "GBK");
    }

}
