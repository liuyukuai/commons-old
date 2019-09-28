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
import org.springframework.security.authentication.LockedException;
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

import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author : liuyk
 */
@Slf4j
@ControllerAdvice
@SuppressWarnings("unused")
public class ExceptionAdvice {

    /**
     * 参数类型不匹配
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, Object> jsonObject = new HashMap<>(fieldErrors.size());
        fieldErrors.forEach((item) -> jsonObject.put(item.getField(), item.getDefaultMessage()));
        return Response.failure(ResponseCode.API_PARAM_ERROR.getCode(), JsonUtil.toJson(jsonObject));
    }


    /**
     * 请求体为空
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Response handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_BODY_IS_EMPTY.getCode(), ResponseCode.API_BODY_IS_EMPTY.getMessage());
    }


    @ResponseBody
    @ExceptionHandler({ConstraintViolationException.class})
    public Response handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        // 只取第一个错误信息给前端，不显示所有，对用户友好
        return Response.failure(ResponseCode.API_PARAM_ERROR.getCode(), constraintViolations.stream().findFirst().map(ConstraintViolation::getMessage).orElse(ResponseCode.SYSTEM_ERROR.getMessage()));
    }


    @ResponseBody
    @ExceptionHandler({ValidationException.class})
    public Response handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_PARAM_ERROR.getCode(), ResponseCode.API_PARAM_ERROR.getMessage());
    }


    @ResponseBody
    @ExceptionHandler({DuplicateKeyException.class})
    public Response<String> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.DATA_IS_EXISTS.getCode(), ResponseCode.DATA_IS_EXISTS.getMessage());
    }


    /**
     * default exception  handler
     *
     * @param e exception
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({RuntimeException.class})
    public Response handlerRuntimeException(RuntimeException e) {
        log.error("default exception msg = [{}] ", e.getMessage(), e);
        return Response.failure(ResponseCode.SYSTEM_ERROR.getCode(), ResponseCode.SYSTEM_ERROR.getMessage());
    }

    /**
     * 请求方式不匹配
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Response handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_UN_SUPPORT_METHOD.getCode(), String.format(ResponseCode.API_UN_SUPPORT_METHOD.getMessage(), e.getMethod()));
    }

    /**
     * 参数类型不匹配
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public Response handleHttpMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_PARAM_TYPE_ERROR.getCode(), String.format(ResponseCode.API_PARAM_TYPE_ERROR.getMessage(), e.getName(), e.getRequiredType()));
    }


    /**
     * 不支持的媒体类型
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public Response handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_UN_SUPPORT_MEDIA_TYPE.getCode(), String.format(ResponseCode.API_UN_SUPPORT_MEDIA_TYPE.getMessage(), e.getContentType()));
    }

    /**
     * 请求资源不存在
     *
     * @param e e
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({NoHandlerFoundException.class})
    public Response handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_NOT_EXISTS.getCode(), String.format(ResponseCode.API_NOT_EXISTS.getMessage(), e.getRequestURL(), e.getHttpMethod()));
    }


    /**
     * 其他servlet exception 处理
     *
     * @param e exception
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({ServletException.class})
    public Response handlerServletException(ServletException e) {
        log.error("default ServletException msg = [{}] ", e.getMessage(), e);
        return Response.failure(ResponseCode.SYSTEM_ERROR);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public Response usernameNotFoundException(UsernameNotFoundException e) {
        log.error("User or password error: ", e);
        return Response.failure(ResponseCode.USER_NOT_EXISTS);
    }


    @ExceptionHandler({NullPointerException.class})
    @ResponseBody
    public Response handleNullPointerException(NullPointerException e) {
        log.error("null point exception : ", e);
        return Response.failure(ResponseCode.API_PARAM_ERROR);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({BindException.class})
    public Response<String> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.API_PARAM_ERROR);
    }


    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({EmptyResultDataAccessException.class})
    public Response<String> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.DATA_NOT_EXISTS);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({BadCredentialsException.class})
    public Response<String> handleBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.USER_ERROR);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @ExceptionHandler({LockedException.class})
    public Response<String> handleBadCredentialsException(LockedException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.USER_LOCKED);
    }

    @ExceptionHandler({FieldRepetitionException.class})
    @ResponseBody
    public Response handleFieldRepetitionException(FieldRepetitionException e) {
        log.error(e.getMessage(), e);
        return Response.failure(ResponseCode.DATA_NAME_IS_EXISTS);
    }
}
