package com.itxiaoer.commons.core;

import com.itxiaoer.commons.core.page.ResponseCode;

/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
public class FieldRepetitionException extends SysException {

    private static final long serialVersionUID = -1305822674064735727L;

    public FieldRepetitionException() {
        super(ResponseCode.DATA_NAME_IS_EXISTS.getCode(), ResponseCode.DATA_NAME_IS_EXISTS.getMessage());
    }

    public FieldRepetitionException(String message) {
        super(ResponseCode.DATA_NAME_IS_EXISTS.getCode(), message);
    }
}
