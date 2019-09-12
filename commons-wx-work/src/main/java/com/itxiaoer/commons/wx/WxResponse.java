package com.itxiaoer.commons.wx;

import com.itxiaoer.commons.core.page.Responsive;
import lombok.Data;

/**
 * @author : liuyk
 */
@Data
public class WxResponse implements Responsive {

    private int errcode;
    
    private String errmsg;

    @Override
    public boolean isSuccess() {
        return errcode == 0;
    }
}
