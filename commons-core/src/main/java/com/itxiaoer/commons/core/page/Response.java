package com.itxiaoer.commons.core.page;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itxiaoer.commons.core.date.LocalDateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
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
public class Response<T> implements Responsive, Serializable {

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
    private String message;


    private String now;

    /**
     * private
     */
    private Response() {
        this.now = LocalDateUtil.format(LocalDateTime.now(),"yyyy-MM-dd hh:mm:ss");
    }

    /**
     * private
     */
    private Response(T data) {
        this.success = true;
        this.data = data;
        this.code = ResponseCode.SUCCESS_CODE;
        this.now = LocalDateUtil.format(LocalDateTime.now(),"yyyy-MM-dd hh:mm:ss");
    }


    private Response(boolean success, String msg) {
        this(success, msg, ResponseCode.SUCCESS_CODE);
    }

    private Response(boolean success, String msg, String code) {
        this.message = msg;
        this.success = success;
        this.code = code;
        this.now = LocalDateUtil.format(LocalDateTime.now(),"yyyy-MM-dd hh:mm:ss");
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
        return Optional.ofNullable(response).map(Response::getMessage).orElse(StringUtils.EMPTY);
    }
}
