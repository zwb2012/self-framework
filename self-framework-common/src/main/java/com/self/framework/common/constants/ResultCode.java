package com.self.framework.common.constants;

/**
 * description: 响应操作码
 *
 * @author wenbo.zhuang
 * @date 2022/02/17 17:11
 **/
public enum  ResultCode implements IResultCode {
    /**
     * 成功
     */
    SUCCESS("200", "操作成功"),
    FAILED("500", "操作失败"),
    VALIDATE_FAILED("404", "参数检验失败"),
    UNAUTHORIZED("401", "暂未登录或token已经过期"),
    FORBIDDEN("403", "没有相关权限"),

    HTTP_FAILED("200001", "HTTP请求失败"),
    PARAM_ERROR("200002", "参数错误"),
    WX_USER_INFO_ERROR("900001", "微信用户信息获取失败"),
    USER_AUTH_ERROR("900021", "用户鉴权信息获取失败"),
    HEALTHY("10000", "链接正常"),
    BIZ_ERROR("300002", "业务异常"),
    ES_ERROR("30001", "ES操作失败"),

    ;

    private final String code;
    private final String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
