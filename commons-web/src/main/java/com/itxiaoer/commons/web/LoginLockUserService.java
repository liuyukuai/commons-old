/*
 *  Copyright@2019 清云智通（北京）科技有限公司 保留所有权利
 */
package com.itxiaoer.commons.web;

/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
public interface LoginLockUserService {

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
}
