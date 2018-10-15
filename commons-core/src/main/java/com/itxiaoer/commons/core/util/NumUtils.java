package com.itxiaoer.commons.core.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 转换工具类
 *
 * @author liuyk
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public final class NumUtils {

    /**
     * private
     */
    private NumUtils() {
    }


    /**
     * 转换为int类型数据
     *
     * @param val 非法字符转换成0
     * @return int
     */
    public static int intVal(String val) {
        return intVal(val, 0);
    }

    /**
     * 转换为int类型数据
     *
     * @param val          需要转换的值
     * @param defaultValue 默认值
     * @return int
     */
    public static int intVal(String val, int defaultValue) {
        try {
            return NumberUtils.createNumber(val).intValue();
        } catch (Exception e) {
            log.warn("intVal(String, int) throw {}, return defaultValue = {}", e, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 转换为long类型数据
     *
     * @param val 非法字符转换成0
     * @return long
     */
    public static long longVal(String val) {
        return longVal(val, 0L);
    }

    /**
     * 转换为long类型数据
     *
     * @param val          需要转换的值
     * @param defaultValue 默认值
     * @return long
     */
    public static long longVal(String val, long defaultValue) {
        try {
            return NumberUtils.createNumber(val).longValue();
        } catch (Exception e) {
            log.warn("longVal(String, int) throw {}, return defaultValue = {}", e, defaultValue);
            return defaultValue;
        }
    }
}
