package com.itxiaoer.commons.core;

import com.itxiaoer.commons.core.page.ErrorCode;

/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
public class NotFoundException extends SysException {

    public NotFoundException() {
        super(ErrorCode.NOT_FOUNT_CODE);
    }

    public NotFoundException(String message) {
        super(ErrorCode.NOT_FOUNT_CODE, message);
    }
}
