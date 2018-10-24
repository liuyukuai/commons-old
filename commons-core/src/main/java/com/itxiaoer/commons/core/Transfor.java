package com.itxiaoer.commons.core;

/**
 * @author : liuyk
 */
public interface Transfor<T, R> {
    /**
     * 转换
     *
     * @param t 原类型
     * @return r 目标类型
     */
    R apply(T t);
}
