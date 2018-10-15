package com.itxiaoer.commons.core.page;

import com.itxiaoer.commons.core.util.NumUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : liuyk
 */
@Slf4j
@SuppressWarnings("WeakerAccess")
public class PageUtils {

    /**
     * 默认最大分页数
     */
    public static final int DEFAULT_MAX_PAGE_SIZE = 5000;

    /**
     * 默认最小分页数
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 默认最大起始页数
     */
    public static final int DEFAULT_MAX_PAGE = 200;

    /**
     * 默认最小起始页数
     */
    public static final int DEFAULT_MIN_PAGE = 1;


    /**
     * 解析start参数
     *
     * @param page page 最大值为DEFAULT_MAX_LIMIT，若page可能超过该值请使用#page(String page, int maxValue)
     * @return page
     */
    public static int page(String page) {
        return page(page, DEFAULT_MAX_PAGE);
    }

    /**
     * 解析page参数
     *
     * @param page     page参数
     * @param maxValue 分页最大值
     * @return page
     */
    public static int page(String page, int maxValue) {
        if (StringUtils.isBlank(page)) {
            return DEFAULT_MIN_PAGE;
        }
        int parseInt = NumUtils.intVal(page);
        return parseInt <= 0 ? DEFAULT_MIN_PAGE : (parseInt > maxValue ? maxValue : parseInt);
    }

    /**
     * 解析分页条数
     *
     * @param size 默认值为20
     * @return size
     */
    public static int size(String size) {
        return size(size, DEFAULT_PAGE_SIZE);
    }

    /**
     * 解析分页条数pageSize
     *
     * @param size         size
     * @param defaultValue pageSize的默认值，limit最大分页数为1000,防止分页数过大出现问题
     * @return size
     */
    public static int size(String size, int defaultValue) {
        int parseInt = NumUtils.intVal(size, defaultValue);
        return parseInt <= 0 ? defaultValue
                : (parseInt > DEFAULT_MAX_PAGE_SIZE ? DEFAULT_MAX_PAGE_SIZE : parseInt);
    }
}
