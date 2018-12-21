package com.itxiaoer.commons.core.util;

import com.itxiaoer.commons.core.date.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
public class UUIDUtils {

    public static String guid() {
        return LocalDateTimeUtil.format(LocalDateTime.now(), "yyyyMMddHHmmssSSSSSS") + UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }
}
