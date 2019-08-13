package com.itxiaoer.commons.core.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
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
        if (StringUtils.isBlank(json)) {
            log.warn("json is blank. ");
            return Optional.empty();
        }
        try {
            OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
        if (Objects.isNull(t)) {
            log.warn("t is blank. ");
            return "";
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(t);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    /**
     * map to object
     *
     * @param params params
     * @param clazz  clazz
     * @param <T>    T
     * @return Optional
     */
    public static <T> Optional<T> toBean(Map<String, Object> params, Class<T> clazz) {
        if (Objects.isNull(params) || params.isEmpty()) {
            log.warn("param is blank. ");
            return Optional.empty();
        }
        try {
            OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return Optional.of(OBJECT_MAPPER.convertValue(params, clazz));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * 将对象转换层map
     *
     * @param e   转换的对象
     * @param <E> 类型
     * @return map
     */
    public static <E> Optional<Map<String, Object>> toMap(E e) {
        if (Objects.isNull(e)) {
            log.warn("param is blank. ");
            return Optional.empty();
        }
        try {
            OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return Optional.of(OBJECT_MAPPER.convertValue(e, new MapTypeReference()));
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    /**
     * map 类型
     */
    private static class MapTypeReference extends TypeReference<Map<String, Object>> {
        private MapTypeReference() {
        }
    }
}
