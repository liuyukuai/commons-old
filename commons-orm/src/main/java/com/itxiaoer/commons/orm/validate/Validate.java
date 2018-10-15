package com.itxiaoer.commons.orm.validate;

import com.itxiaoer.commons.core.ParameterException;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author : liuyk
 */
@SuppressWarnings({"unused"})
public interface Validate<E, ID> {
    /**
     * 校验方法
     *
     * @param e 校验的元素
     */
    default void valid(E e) {

    }

    /**
     * 校验id方法
     *
     * @param id id
     */
    default void idValid(ID id) {
        if (Objects.isNull(id)) {
            throw new ParameterException("the id is null. ");
        }
        boolean validate = id instanceof String && StringUtils.isBlank((String) id);
        if (!validate) {
            throw new ParameterException("the id is null. ");
        }
    }
}
