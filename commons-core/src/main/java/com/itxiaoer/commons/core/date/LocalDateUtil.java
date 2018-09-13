package com.itxiaoer.commons.core.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;

/**
 * @author : liuyk
 */
@SuppressWarnings("all")
public final class LocalDateUtil {
    /**
     * default
     */
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd";


    private LocalDateUtil() {
    }

    /**
     * 本月第一天
     *
     * @return localDate
     */
    public static LocalDate firstDay() {
        return firstDay(LocalDate.now());
    }

    /**
     * 指定时间的当月第一天
     *
     * @param localDate 时间
     * @return localDate
     */
    public static LocalDate firstDay(LocalDate localDate) {
        return LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
    }


    /**
     * 指定时间的当月第一天
     *
     * @param localDate 时间
     * @return localDate
     */
    public static LocalDate firstDay(String localDate) {
        LocalDate parse = parse(localDate, DEFAULT_PATTERN);
        return LocalDate.of(parse.getYear(), parse.getMonth(), 1);
    }

    /**
     * 本月最后一天
     *
     * @return localDate
     */
    public static LocalDate lastDay() {
        return lastDay(LocalDate.now());
    }

    /**
     * 本月最后一天
     *
     * @param localDate 时间
     * @return localDate
     */
    public static LocalDate lastDay(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }


    /**
     * 本月最后一天
     *
     * @param localDate 时间
     * @return localDate
     */
    public static LocalDate lastDay(String localDate) {
        LocalDate parse = parse(localDate, DEFAULT_PATTERN);
        return parse.with(TemporalAdjusters.lastDayOfMonth());
    }


    /**
     * addDays
     *
     * @param localDate 时间
     * @return localDate
     */
    public static LocalDate addDays(String localDate, long days) {
        LocalDate parse = parse(localDate, DEFAULT_PATTERN);
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
    public static LocalDate parse(String localDate, String pattern) {
        return LocalDate.from(DateTimeFormatter.ofPattern(pattern).parse(localDate));
    }

}
