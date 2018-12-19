package com.itxiaoer.commons.web;

import com.itxiaoer.commons.core.json.JsonUtil;
import com.itxiaoer.commons.core.page.Response;
import com.itxiaoer.commons.core.page.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author : liuyk
 */
@Slf4j
@ControllerAdvice
@SuppressWarnings("unused")
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public Response handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("不支持当前请求方法:{}", e);
        return Response.failure(String.format("不支持%s请求方式：", new Object[]{request.getMethod()}));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public Response UsernameNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);
        return Response.failure("登录信息验证失败：" + e.getMessage(), ResponseCode.NO_PERMISSION);
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseBody
    public Response handleValidationException(ValidationException e) {
        log.error("参数验证失败:{}", e);
        return Response.failure("参数验证失败：" + e.getMessage(), ResponseCode.PARAMETER_VALID_CODE);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public Response handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("参数解析失败{}", e);
        return Response.failure("参数解析失败：" + e.getMessage(), ResponseCode.PARAMETER_VALID_CODE);
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    public Response handleHttpMediaTypeNotSupportedException(Exception e) {
        log.error("不支持当前媒体类型", e);
        return Response.failure("不支持当前媒体类型");
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseBody
    public Response handleHttpMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("参数解析失败{}", e);
        return Response.failure("参数解析失败：" + e.getMessage(), ResponseCode.PARAMETER_VALID_CODE);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseBody
    public Response handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("资源不存在{}", e);
        return Response.failure("资源不存在：" + e.getMessage());
    }

    @ExceptionHandler({NullPointerException.class})
    @ResponseBody
    public Response handleNullPointerException(NullPointerException e) {
        log.error("空指针异常:{}", e);
        return Response.failure("空指针异常：" + e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Response<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        ArrayList messages = new ArrayList(fieldErrors.size());
        fieldErrors.forEach((item) -> {
            HashMap jsonObject = new HashMap(fieldErrors.size());
            jsonObject.put(item.getField(), item.getDefaultMessage());
            messages.add(jsonObject);
        });
        log.error("参数异常: method={}", e.getParameter().getMethod());
        log.error("参数异常: detail={} ", messages);
        return Response.failure(JsonUtil.toJson(messages), ResponseCode.PARAMETER_VALID_CODE);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({IllegalArgumentException.class})
    public Response<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return Response.failure("参数校验失败：" + e.getMessage(), ResponseCode.PARAMETER_VALID_CODE);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({UnsupportedEncodingException.class})
    public Response<String> handleUnsupportedEncodingException(UnsupportedEncodingException e) {
        return Response.failure("数据解码失败：" + e.getMessage());
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({DuplicateKeyException.class})
    public Response<String> handleDuplicateKeyException(DuplicateKeyException e) {
        return Response.failure("参数重复：" + e.getMessage(), ResponseCode.PARAMETER_VALID_CODE);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({BindException.class})
    public Response<String> handleBindException(BindException e) {
        return Response.failure("参数校验失败：" + e.getMessage(), ResponseCode.PARAMETER_VALID_CODE);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({ConstraintViolationException.class})
    public Response<String> handleConstraintViolationException(ConstraintViolationException e) {
        return Response.failure("参数校验失败：" + e.getMessage(), ResponseCode.PARAMETER_VALID_CODE);
    }
}
