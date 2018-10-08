package com.itxiaoer.commons.core.util;

import com.itxiaoer.commons.core.constant.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author : liuyk
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Md5Utils {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c', 'd', 'e', 'f'};

    /**
     * 计算MD5值
     *
     * @param message 字符串
     * @return MD5
     */
    public static String digestMD5(String message) {
        return digestMD5(message, Constants.UTF_8);
    }

    /**
     * 计算MD5值
     *
     * @param message 字符串
     * @param charset 编码
     * @return MD5
     */
    public static String digestMD5(String message, String charset) {
        try {
            byte[] strTemp = message.getBytes(charset);
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (byte b : md) {
                chars[k++] = HEX_DIGITS[b >>> 4 & 0xf];
                chars[k++] = HEX_DIGITS[b & 0xf];
            }
            return new String(chars);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * 文件流求MD5
     *
     * @param inputStream 文件流
     * @return MD5
     */
    public static String digestMD5(InputStream inputStream) {
        int size = 4096;
        int readLen = 32;
        StringBuilder md5String = new StringBuilder();
        try {
            byte[] buffer = new byte[size];
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            int len;
            while ((len = inputStream.read(buffer, 0, size)) != -1) {
                md5.update(buffer, 0, len);
            }
            BigInteger bigInteger = new BigInteger(1, md5.digest());
            md5String.append(bigInteger.toString(16));
            if (md5String.length() < readLen) {
                int j = readLen - md5String.length();
                for (int i = 0; i < j; i++) {
                    md5String.insert(0, "0");
                }
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new IllegalArgumentException(e);
        }
        return md5String.toString();
    }
}
