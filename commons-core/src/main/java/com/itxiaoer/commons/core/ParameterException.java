package com.itxiaoer.commons.core;

import com.itxiaoer.commons.core.page.ResponseCode;

/**
 * 参数校验异常
 *
 * @author : liuyk
 */
@SuppressWarnings("unused")
public class ParameterException extends SysException {

    private static final long serialVersionUID = -517633067951061993L;

    public ParameterException() {
        super(ResponseCode.API_PARAM_ERROR.getCode(), ResponseCode.API_PARAM_ERROR.getMessage());
    }

    public ParameterException(String message) {
        super(ResponseCode.API_PARAM_ERROR.getCode(), message);
    }
}
