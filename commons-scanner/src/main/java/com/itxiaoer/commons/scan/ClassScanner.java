package com.itxiaoer.commons.scan;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 查找指定目录下的所有类
 *
 * @author : liuyk
 */


@SuppressWarnings({"unused", "WeakerAccess"})
public final class ClassScanner {

    private static final String SUFFIX = ".class";

    /**
     * 扫描目录下的所有class
     *
     * @param dir 目录
     * @return list classes
     */
    public static List<Class<?>> scan(String dir) {
        return scan(dir, null);
    }


    /**
     * 扫描目录下的所有class
     *
     * @param dir             目录
     * @param annotationClass 注解
     * @return list classes
     */
    public static List<Class<?>> scan(String dir, Class<? extends Annotation> annotationClass) {

        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        URLClassLoader classLoader;
        try {
            classLoader = new URLClassLoader(new URL[]{new URL("file://" + dir + File.separator)}, systemClassLoader);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return scan(dir, classLoader, annotationClass);
    }

    /**
     * 扫描目录下的所有class
     *
     * @param dir             目录
     * @param annotationClass 注解
     * @param classLoader     classloader
     * @return list classes
     */
    public static List<Class<?>> scan(String dir, URLClassLoader classLoader, Class<? extends Annotation> annotationClass) {

        List<File> files = FileScanner.scan(dir, SUFFIX);
        return files
                .stream()
                .map(e -> ClassScanner.getClassName(dir, e))
                .map(e -> {
                    try {
                        return classLoader.loadClass(e);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .filter(e -> {

                    Annotation[] annotations = e.getAnnotations();
                    Field[] fields = e.getFields();
                    return Optional.ofNullable(annotationClass).map(x -> e.getAnnotation(x) != null).orElse(true);
                })
                .collect(Collectors.toList());
    }


    /**
     * 获取className
     *
     * @param dir  文件夹路径
     * @param file 文件
     * @return class名称
     */
    public static String getClassName(String dir, File file) {
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        String replace = file.getAbsolutePath().replace(dir, "");
        replace = replace.replace(File.separator, ".");
        replace = replace.substring(0, replace.lastIndexOf(SUFFIX));
        return replace;
    }
}
