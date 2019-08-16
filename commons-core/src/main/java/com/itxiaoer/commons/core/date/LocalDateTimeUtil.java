package com.itxiaoer.commons.core.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;

/**
 * @author : liuyk
 */
@SuppressWarnings("all")
public final class LocalDateTimeUtil {
    /**
     * default
     */
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";


    private LocalDateTimeUtil() {
    }

    /**
     * 本月第一天
     *
     * @return localDate
     */
    public static LocalDateTime firstDay() {
        return firstDay(LocalDateTime.now());
    }

    /**
     * 指定时间的当月第一天
     *
     * @param localDate 时间
     * @return localDate
     */
    public static LocalDateTime firstDay(LocalDateTime localDate) {
        return LocalDateTime.of(localDate.getYear(), localDate.getMonth(), 1, 0, 0, 0);
    }


    /**
     * 指定时间的当月第一天
     *
     * @param localDate 时间
     * @return localDate
     */
    public static LocalDateTime firstDay(String localDate) {
        LocalDateTime parse = parse(localDate, DEFAULT_PATTERN);
        return LocalDateTime.of(parse.getYear(), parse.getMonth(), 1, 0, 0, 0);
    }

    /**
     * 本月最后一天
     *
     * @return localDate
     */
    public static LocalDateTime lastDay() {
        return lastDay(LocalDateTime.now());
    }

    /**
     * 本月最后一天
     *
     * @param localDate 时间
     * @return localDate
     */
    public static LocalDateTime lastDay(LocalDateTime localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }


    /**
     * 本月最后一天
     *
     * @param localDate 时间
     * @return localDate
     */
    public static LocalDateTime lastDay(String localDate) {
        LocalDateTime parse = parse(localDate, DEFAULT_PATTERN);
        return parse.with(TemporalAdjusters.lastDayOfMonth());
    }


    /**
     * addDays
     *
     * @param localDate 时间
     * @param days      天数
     * @return localDate
     */
    public static LocalDateTime addDays(String localDate, long days) {
        LocalDateTime parse = parse(localDate, DEFAULT_PATTERN);
        return parse.plusDays(days);
    }

    /**
     * 格式化时间
     *
     * @param localDate 时间
     * @param pattern   时间格式
     * @return 时间字符串
     */
    public static String format(TemporalAccessor localDate, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(localDate);
    }


    /**
     * 格式化时间
     *
     * @param localDate 时间
     * @param pattern   时间格式
     * @return 时间字符串
     */
    public static LocalDateTime parse(String localDate, String pattern) {
        return LocalDateTime.from(DateTimeFormatter.ofPattern(pattern).parse(localDate));
    }

}
