package com.self.framework.common.exception;


import com.self.framework.common.constants.IResultCode;

/**
 * description: 自定义异常
 *
 * @author wenbo.zhuang
 * @date 2022/02/21 14:15
 **/
public class BusinessException extends RuntimeException {

    private String code;


    public BusinessException(IResultCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }


    public BusinessException(Throwable cause) {
        super(cause);
    }
}
