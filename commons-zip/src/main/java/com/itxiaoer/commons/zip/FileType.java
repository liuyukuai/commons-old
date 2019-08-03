package com.itxiaoer.commons.zip;


import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;

/**
 * 支持文件解压类型
 *
 * @author : liuyk
 */
@Slf4j
public enum FileType {

    // 未知
    UNKNOWN,
    // 压缩文件
    ZIP, RAR, _7Z, TAR, GZ, TAR_GZ, BZ2, TAR_BZ2;

    /**
     * 获取文件真实类型
     *
     * @param file 要获取类型的文件。
     * @return 文件类型枚举。
     */

    public static FileType fileType(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] head = new byte[4];
            if (-1 == inputStream.read(head)) {
                return FileType.UNKNOWN;
            }
            int headHex = 0;
            for (byte b : head) {
                headHex <<= 8;
                headHex |= b;
            }
            switch (headHex) {
                case 0x504B0304:
                    return FileType.ZIP;
                case 0x776f7264:
                    return FileType.TAR;
                case -0x51:
                    return FileType._7Z;
                case 0x425a6839:
                    return FileType.BZ2;
                case -0x74f7f8:
                    return FileType.GZ;
                case 0x52617221:
                    return FileType.RAR;
                default:
                    return FileType.UNKNOWN;
            }

        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return FileType.UNKNOWN;
    }
}
