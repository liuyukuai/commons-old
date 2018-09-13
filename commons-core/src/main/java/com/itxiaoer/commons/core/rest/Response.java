package com.itxiaoer.commons.core.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 响应结果对象
 *
 * @author liuyk
 */
@Data
@SuppressWarnings({"unused", "all"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -3212041374480698935L;

    private static Logger logger = LoggerFactory.getLogger(Response.class);

    private boolean success;

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


    public static <E> Response<E> ok() {
        return new Response<>(null);
    }

    public static <E> Response<E> ok(E data) {
        return new Response<>(data);
    }

    public static <E> Response<E> no() {
        return new Response<>(false, "");
    }


    public static <E> Response<E> no(String msg) {
        return new Response<>(false, msg);
    }


    public static <E> Response<E> no(String msg, String code) {
        return new Response<>(false, msg);
    }

}
