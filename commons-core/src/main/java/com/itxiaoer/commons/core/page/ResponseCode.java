package com.itxiaoer.commons.core.page;

/**
 * @author : liuyk
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class ResponseCode {
    /**
     * 成功code值
     */
    public final static String SUCCESS_CODE = "0";
    /**
     * 没有权限code值
     */
    public final static String NO_PERMISSION = "40001";
    /**
     * 参数校验失败code
     */
    public final static String PARAMETER_VALID_CODE = "40002";
    /**
     * 密码错误code值
     */
    public final static String LOGIN_PASSWORD_ERROR_CODE = "40003";
    /**
     * 实体不存在code值
     */
    public final static String NOT_FOUNT_CODE = "40004";
    /**
     * 属性重复code值
     */
    public final static String FIELD_REPETITION_CODE = "40005";

    /**
     * 服务器内部错误
     */
    public final static String SERVER_ERROR_CODE = "50000";

}
