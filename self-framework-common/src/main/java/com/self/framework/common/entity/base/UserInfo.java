package com.self.framework.common.entity.base;

import com.self.framework.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * description: 用户信息
 *
 * @author wenbo.zhuang
 * @date 2022/02/17 17:39
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfo extends BaseEntity {

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像路径
     */
    private String avatarUrl;

    /**
     * 用户最近登录时间
     */
    private Date loginTime;
}
