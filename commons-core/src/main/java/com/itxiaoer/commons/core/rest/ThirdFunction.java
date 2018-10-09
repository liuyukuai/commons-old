package com.itxiaoer.commons.core.rest;

/**
 * ThirdFunction
 *
 * @author : liuyk
 */
@FunctionalInterface
public interface ThirdFunction<R, F, S, T> {
    /**
     * 函数执行方法
     *
     * @param f first参数
     * @param s second参数
     * @param t third参数
     * @return 返回值
     */
    R apply(F f, S s, T t);

}
