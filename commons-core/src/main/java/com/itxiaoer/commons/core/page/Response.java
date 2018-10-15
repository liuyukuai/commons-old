package com.itxiaoer.commons.core.page;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Optional;


/**
 * 响应结果对象
 *
 * @author liuyk
 */
@Slf4j
@Data
@SuppressWarnings("unused")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -3212041374480698935L;

    private boolean success;

    /**
     * data
     */
    private T data;

    /**
     * code
     */
    private String code;
    /**
     * message
     */
    private String msg;

    /**
     * private
     */
    private Response() {
    }

    /**
     * private
     */
    private Response(T data) {
        this.success = true;
        this.data = data;
    }


    private Response(boolean success, String msg) {
        this(success, msg, "0");
    }

    private Response(boolean success, String msg, String code) {
        this.msg = msg;
        this.success = success;
        this.code = code;
    }

    /**
     * success response
     *
     * @param <E> data type
     * @return response
     */
    public static <E> Response<E> ok() {
        return new Response<>(null);
    }

    /**
     * success response
     *
     * @param <E>  data type
     * @param data data
     * @return response
     */
    public static <E> Response<E> ok(E data) {
        return new Response<>(data);
    }

    /**
     * failure response
     *
     * @param <E> data type
     * @return response
     */
    public static <E> Response<E> failure() {
        return new Response<>(false, "");
    }

    /**
     * failure response
     *
     * @param <E>     data type
     * @param message failure message
     * @return response
     */

    public static <E> Response<E> failure(String message) {
        return new Response<>(false, message);
    }

    /**
     * failure response
     *
     * @param <E>     data type
     * @param message failure message
     * @param code    failure code
     * @return response
     */

    public static <E> Response<E> failure(String message, String code) {
        return new Response<>(false, message, code);
    }

    /**
     * get response message
     *
     * @param response response
     * @param <E>      e
     * @return message
     */
    public static <E> String message(Response<E> response) {
        return Optional.ofNullable(response).map(Response::getMsg).orElse(StringUtils.EMPTY);
    }
}
