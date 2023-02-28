package com.self.framework.common.config;

import com.self.framework.common.constants.ResultCode;
import com.self.framework.common.api.response.Response;
import com.self.framework.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 通用异常处理
 *
 * @author wenbo.zhuang
 * @date 2022/06/17 16:35
 **/
@Slf4j
@RestControllerAdvice
@SuppressWarnings("rawtypes")
public class CommonExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public Response exception(Exception e) {
        // 记录日志
        log.error(e.getMessage(), e);
        // 返回通用异常信息
        return Response.failed(ResultCode.BIZ_ERROR);
    }

    /**
     * 自定义异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public Response leadExceptionHandler(BusinessException exception) {
        log.error("catch exception:{}", exception.getMessage(), exception);
        return Response.failed(exception.getCode(), exception.getMessage());
    }
}
