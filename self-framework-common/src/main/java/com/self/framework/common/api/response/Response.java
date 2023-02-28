package com.self.framework.common.api.response;


import com.self.framework.common.constants.IResultCode;
import com.self.framework.common.constants.ResultCode;

/**
 * description: 通用返回接口
 *
 * @author wenbo.zhuang
 * @date 2022/02/17 17:09
 **/
public class Response<T> {

    /**
     * 状态码
     */
    private String code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 数据封装
     */
    private T data;

    protected Response() {
    }

    protected Response(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> Response<T> success(T data) {
        return new Response<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> Response<T> success(T data, String message) {
        return new Response<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> Response<T> failed(IResultCode errorCode) {
        return new Response<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static <T> Response<T> failed(IResultCode errorCode, String message) {
        return new Response<>(errorCode.getCode(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param code    错误码
     * @param message 提示信息
     */
    public static <T> Response<T> failed(String code, String message) {
        return new Response<>(code, message, null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Response<T> failed(String message) {
        return new Response<>(ResultCode.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> Response<T> failed() {
        return failed(ResultCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Response<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Response<T> validateFailed(String message) {
        return new Response<>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 通用返回
     *
     * @param errorCode 返回码
     */
    public static <T> Response<T> result(IResultCode errorCode) {
        return new Response<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> Response<T> unauthorized(T data) {
        return new Response<>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> Response<T> forbidden(T data) {
        return new Response<>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
