/*
 *  Copyright@2019 清云智通（北京）科技有限公司 保留所有权利
 */
package com.itxiaoer.commons.web;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
public interface Lockable {


    Map<String, AtomicInteger> COUNTER = new ConcurrentHashMap<>();


    /**
     * 锁定当前登录用户
     *
     * @param loginName 登录名
     */
    void lock(String loginName);

    /**
     * 密码输入错误次数
     *
     * @return 密码错误次数设置
     */
    Integer wrongTimes();

    /**
     * 增加密码错误次数
     *
     * @param loginName loginName
     */
    static void incrementTimes(String loginName) {
        AtomicInteger integer = COUNTER.get(loginName);
        if (Objects.isNull(integer)) {
            integer = new AtomicInteger(1);
            COUNTER.put(loginName, integer);
        } else {
            integer.incrementAndGet();
            COUNTER.put(loginName, integer);
        }
    }
}
