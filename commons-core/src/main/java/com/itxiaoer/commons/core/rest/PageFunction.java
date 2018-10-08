package com.itxiaoer.commons.core.rest;

/**
 * PageFunction
 *
 * @author : liuyk
 */
@FunctionalInterface
public interface PageFunction<R, F, S, T> {

    R apply(F f, S s, T t);

}
