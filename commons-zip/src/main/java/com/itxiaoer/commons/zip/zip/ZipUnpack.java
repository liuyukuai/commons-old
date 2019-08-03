package com.itxiaoer.commons.zip.zip;

import com.itxiaoer.commons.zip.Files;
import com.itxiaoer.commons.zip.Unpack;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;

/**
 * @author : liuyk
 */
@Slf4j
public class ZipUnpack implements Unpack {

    @Override
    public void unpack(File file, String destPath, String encoding) {
        Files.mkdirs(destPath);

        try (ZipArchiveInputStream is = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(file), 8192), encoding)) {
            ZipArchiveEntry entry;

            while ((entry = is.getNextZipEntry()) != null) {

                File dir = new File(destPath, entry.getName());
                if (entry.isDirectory()) {
                    Files.mkdirs(dir);
                } else {
                    Files.mkdirs(dir.getParent());
                    try (OutputStream os = new BufferedOutputStream(new FileOutputStream(dir), 8192)) {
                        IOUtils.copy(is, os);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Unzip failed, message:[{}]", e.getMessage());
        }
    }

}
