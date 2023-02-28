package com.self.framework.common.constants;


/**
 * description: 封装API的错误码
 *
 * @author wenbo.zhuang
 * @date 2022/02/17 17:11
 **/
public interface IResultCode {
    /**
     * 获取操作返回码
     *
     * @return long
     */
    String getCode();

    /**
     * 获取操作返回信息
     *
     * @return String
     */
    String getMessage();
}
