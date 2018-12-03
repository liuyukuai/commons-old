package com.itxiaoer.commons.core;

import com.itxiaoer.commons.core.page.ResponseCode;

/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
public class NotFoundException extends SysException {

    public NotFoundException() {
        super(ResponseCode.NOT_FOUNT_CODE);
    }

    public NotFoundException(String message) {
        super(ResponseCode.NOT_FOUNT_CODE, message);
    }
}
