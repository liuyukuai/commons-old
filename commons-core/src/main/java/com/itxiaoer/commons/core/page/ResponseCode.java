package com.itxiaoer.commons.core.page;

/**
 * @author : liuyk
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ResponseCode {
    /**
     * 成功
     */
    SUCCESS("0", "ok"),

    /**
     * 系统错误
     */
    SYSTEM_ERROR("10001", "系统出现异常，请稍后重试。"),

    /* API 相关异常 */
    /**
     * 没有权限
     */
    API_PERMISSION_DENIED("10002", "对不起，您没有权限访问，请联系管理员。"),

    /**
     * 系统错误
     */
    API_UN_SUPPORT_MEDIA_TYPE("10003", "不支持的MediaType (%s)"),

    /**
     * 参数值格式不正确
     */
    API_PARAM_ERROR("10004", "参数值格式不正确"),


    /**
     * 参数值类型不正确
     */
    API_PARAM_TYPE_ERROR("10005", "参数%s只能是%s类型数据"),

    /**
     * 请求体不能为空
     */
    API_BODY_IS_EMPTY("10006", "请求体不能为空"),

    /**
     * 不支持该请求方式
     */
    API_UN_SUPPORT_METHOD("10007", "不支持%s请求方式"),

    /**
     * 请求地址不存在
     */
    API_NOT_EXISTS("10008", "请请求资源[%s] - [%s]不存在"),

    /*登录认证相关*/

    /**
     * 用户名或密码不正确
     */
    USER_ERROR("20001", "用户名或密码不正确"),

    /**
     * 用户已经被禁用
     */
    USER_NOT_EXISTS("20002", "用户不存在"),

    /**
     * 用户已经被禁用
     */
    USER_DISABLED("20003", "用户已经被禁用，请联系管理员激活"),

    /**
     * 用户token值已经过期
     */
    USER_TOKEN_EXPIRED("20004", "登录已经失效，请重新登录。"),

    /**
     * 用户token非法，不能解析
     */
    USER_TOKEN_INVALID("20005", "登录信息非法，请重新登录。"),

    /**
     * 用户未登录
     */
    USER_NOT_LOGIN("20006", "您尚未登录，请先登录。"),

    /**
     * 用户没有操作权限
     */
    USER_NO_PERMISSION("20007", "您没有操作权限，请联系管理员。"),

    /**
     * 用户登录名为空
     */
    USER_LOGIN_NAME_EMPTY("20008", "用户登录名不能为空"),

    /**
     * 用户登录名格式不正确
     */
    USER_LOGIN_NAME_INVALID("20009", "用户登录名不正确，%s"),

    /**
     * 用户登录名已经存在
     */
    USER_LOGIN_NAME_EXISTS("20010", "用户登录名已经存在"),

    /**
     * 用户密码为空
     */
    USER_LOGIN_PASSWORD_EMPTY("20011", "用户密码不能为空"),

    /**
     * 用户密码格式不正确
     */
    USER_LOGIN_PASSWORD_INVALID("20012", "用户密码不正确"),

    /**
     * 用户已经被锁定
     */
    USER_LOCKED("20013", "用户已经被锁定，请联系管理员解锁"),


    /**
     * 验证码错误
     */
    USER_LOGIN_CODE_ERROR("20014", "请输入正确的验证码"),


    /**
     * 数据主键为空
     */
    DATA_FIELD_IS_NULL("30001", "%s不能为空"),

    /**
     * 数据已经存在
     */
    DATA_IS_EXISTS("30002", "%s已经存在"),

    /**
     * 数据不存在
     */
    DATA_NOT_EXISTS("30003", "%s不存在"),


    /**
     * 数据有关联不能删除
     */
    DATA_RELATION_CAN_NOT_DELETE("30004", "%s有%s，不能删除"),

    /**
     * 数据名称重复
     */
    DATA_NAME_IS_EXISTS("30004", "%s名称重复"),

    /**
     * redis服务宕机
     */
    REDIS_SERVICE_IS_DOWN("40001", "缓存服务器不可用。"),

    /**
     * redis服务超时
     */
    REDIS_SERVICE_TIMEOUT("40002", "缓存服务器操作超时。"),


    /**
     * 服务不可用
     */
    SERVICE_IS_DOWN("50001", "%s is down");

    /**
     * 编号
     */
    private String code;

    /**
     * 消息
     */
    private String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
