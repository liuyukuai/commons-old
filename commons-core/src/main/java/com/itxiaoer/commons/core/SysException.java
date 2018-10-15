package com.itxiaoer.commons.core;

import lombok.Getter;

/**
 * @author : liuyk
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class SysException extends RuntimeException {
    /**
     * 异常错误编码
     */
    @Getter
    private String code;

    public SysException() {

    }

    public SysException(String code) {
        this(code, "");
    }


    public SysException(String code, String message) {
        super(message);
        this.code = code;
    }

}
