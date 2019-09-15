package com.itxiaoer.commons.web;

import com.itxiaoer.commons.core.FieldRepetitionException;
import com.itxiaoer.commons.core.json.JsonUtil;
import com.itxiaoer.commons.core.page.Response;
import com.itxiaoer.commons.core.page.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author : liuyk
 */
@Slf4j
@ControllerAdvice
@SuppressWarnings("unused")
public class ExceptionAdvice {

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public Response handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("The request method {} is not supported ", request.getMethod(), e);
        return Response.failure(String.format("The request method %s is not supported ", request.getMethod()), ResponseCode.PARAMETER_VALID_CODE);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public Response usernameNotFoundException(UsernameNotFoundException e) {
        log.error("User or password error: ", e);
        return Response.failure(String.format("User or password error.  %s", e.getMessage()), ResponseCode.LOGIN_PASSWORD_ERROR_CODE);
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseBody
    public Response handleValidationException(ValidationException e) {
        log.error("Parameter validation failed : ", e);
        return Response.failure(String.format("Parameter validation failed : %s", e.getMessage()), ResponseCode.PARAMETER_VALID_CODE);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public Response handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("Parameter validation failed : ", e);
        return Response.failure(String.format("Parameter validation failed : %s", e.getMessage()), ResponseCode.PARAMETER_VALID_CODE);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    public Response handleHttpMediaTypeNotSupportedException(Exception e) {
        log.error("The current media type is not supported:  ", e);
        return Response.failure("The request method %s is not supported ", ResponseCode.SERVER_ERROR_CODE);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseBody
    public Response handleHttpMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("Parameter validation failed : ", e);
        return Response.failure("Parameter validation failed : " + e.getMessage(), ResponseCode.PARAMETER_VALID_CODE);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseBody
    public Response handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("Resources don't exist: ", e);
        return Response.failure(String.format("Resources don't exist : %s", e.getMessage()), ResponseCode.NOT_FOUNT_CODE);
    }

    @ExceptionHandler({NullPointerException.class})
    @ResponseBody
    public Response handleNullPointerException(NullPointerException e) {
        log.error("null point exception : ", e);
        return Response.failure(String.format("Resources don't exist : %s", e.getMessage()), ResponseCode.SERVER_ERROR_CODE);
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Response<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        ArrayList<Map<String, Object>> messages = new ArrayList<>(fieldErrors.size());
        fieldErrors.forEach((item) -> {
            Map<String, Object> jsonObject = new HashMap<>(fieldErrors.size());
            jsonObject.put(item.getField(), item.getDefaultMessage());
            messages.add(jsonObject);
        });
        log.error("Parameter validation failed:  ", e);
        return Response.failure(JsonUtil.toJson(messages), ResponseCode.PARAMETER_VALID_CODE);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({IllegalArgumentException.class})
    public Response<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Parameter validation failed : ", e);
        return Response.failure(String.format("Parameter validation failed : %s", e.getMessage()), ResponseCode.PARAMETER_VALID_CODE);
    }


    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({DuplicateKeyException.class})
    public Response<String> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("The primary key repeat : ", e);
        return Response.failure(String.format("The primary key repeat : %s", e.getMessage()), ResponseCode.PARAMETER_VALID_CODE);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({BindException.class})
    public Response<String> handleBindException(BindException e) {
        log.error("Parameter validation failed : ", e);
        return Response.failure(String.format("Parameter validation failed : %s", e.getMessage()), ResponseCode.PARAMETER_VALID_CODE);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({ConstraintViolationException.class})
    public Response<String> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Parameter validation failed : ", e);
        return Response.failure(String.format("Parameter validation failed : %s", e.getMessage()), ResponseCode.PARAMETER_VALID_CODE);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({EmptyResultDataAccessException.class})
    public Response<String> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        log.error("Parameter validation failed : ", e);
        return Response.failure(String.format("Parameter validation failed : %s", e.getMessage()), ResponseCode.NOT_FOUNT_CODE);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({BadCredentialsException.class})
    public Response<String> handleBadCredentialsException(BadCredentialsException e) {
        log.error("Parameter validation failed : ", e);
        return Response.failure("用户名或密码错误，请重新输入");
    }


    @ExceptionHandler({FieldRepetitionException.class})
    @ResponseBody
    public Response handleFieldRepetitionException(FieldRepetitionException e) {
        log.error("Parameter validation failed : ", e);
        return Response.failure(String.format("Parameter validation failed : %s", e.getMessage()), ResponseCode.FIELD_REPETITION_CODE);
    }
}
