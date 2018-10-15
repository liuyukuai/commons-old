package com.itxiaoer.commons.core.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author : liuyk
 */
@Slf4j
@SuppressWarnings("unused")
public final class JsonUtil {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * json to object
     *
     * @param json  json
     * @param clazz clazz
     * @param <T>   T
     * @return Optional
     */
    public static <T> Optional<T> toBean(String json, Class<T> clazz) {
        try {
            return Optional.of(OBJECT_MAPPER.readValue(json, clazz));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }


    /**
     * object to json
     *
     * @param <T> T t
     * @param t   参数类型
     * @return string
     */
    public static <T> String toJson(T t) {
        try {
            return OBJECT_MAPPER.writeValueAsString(t);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }
}
