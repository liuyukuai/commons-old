package com.itxiaoer.commons.core;

import com.itxiaoer.commons.core.page.ResponseCode;

/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
public class NotFoundException extends SysException {

    private static final long serialVersionUID = 5037516519804075943L;

    public NotFoundException() {
        super(ResponseCode.DATA_NOT_EXISTS.getCode(), ResponseCode.DATA_NOT_EXISTS.getMessage());
    }

    public NotFoundException(String message) {
        super(ResponseCode.DATA_NOT_EXISTS.getCode(), message);
    }
}
