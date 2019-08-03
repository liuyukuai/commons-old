package com.itxiaoer.commons.zip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 解压接口
 *
 * @author : liuyk
 */
@FunctionalInterface
public interface Unpack {

    /**
     * 解压文件接口
     *
     * @param file     需要解压的文件
     * @param destPath 文件存放路径
     * @param encoding 文件编码
     */
    void unpack(File file, String destPath, String encoding) throws IOException;
}
