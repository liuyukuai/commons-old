package com.itxiaoer.commons.core;

import com.itxiaoer.commons.core.page.ResponseCode;

/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
public class FieldRepetitionException extends SysException {

    public FieldRepetitionException() {
        super(ResponseCode.FIELD_REPETITION_CODE);
    }

    public FieldRepetitionException(String message) {
        super(ResponseCode.FIELD_REPETITION_CODE, message);
    }
}
