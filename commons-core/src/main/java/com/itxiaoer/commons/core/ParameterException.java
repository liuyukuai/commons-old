package com.itxiaoer.commons.core;

import com.itxiaoer.commons.core.page.ErrorCode;

/**
 * 参数校验异常
 *
 * @author : liuyk
 */
@SuppressWarnings("unused")
public class ParameterException extends SysException {

    public ParameterException() {
        super(ErrorCode.PARAMETER_VALID_CODE);
    }

    public ParameterException(String message) {
        super(ErrorCode.PARAMETER_VALID_CODE, message);
    }
}
