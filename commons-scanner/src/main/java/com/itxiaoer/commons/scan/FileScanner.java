package com.itxiaoer.commons.scan;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 扫描文件
 *
 * @author : liuyk
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public final class FileScanner {


    /**
     * 遍历目录下的文件
     *
     * @param dir    目录
     * @param suffix 文件后缀
     * @return file list
     */
    public static List<File> scan(String dir, String suffix) {
        // if dir is null;
        if (Objects.isNull(dir) || dir.isEmpty()) {
            return Collections.emptyList();
        }

        Path path = Paths.get(dir);
        List<File> classes = new ArrayList<>(64);
        try {
            Files.walkFileTree(path, new FindJavaVisitor(suffix, classes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }


    /**
     * 遍历目录下的文件
     *
     * @param dir 目录
     * @return file list
     */
    public static List<File> scan(String dir) {
        return scan(dir, "");
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class FindJavaVisitor extends SimpleFileVisitor<Path> {

        private String suffix;

        private List<File> classes;


        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            String fileName = file.toString();
            if (fileName.endsWith(suffix)) {
                classes.add(file.toFile());
            }
            return FileVisitResult.CONTINUE;
        }

    }
}
