package com.self.framework.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * description: 基础pojo
 *
 * @author wenbo.zhuang
 * @date 2022/02/26 19:34
 **/
@Data
public class BaseEntity implements Serializable {

    /**
     * 唯一标识
     */
    @JsonIgnore
    private Long id;

    /**
     * 记录创建时间
     */
    @JsonIgnore
    private Date createTime;

    /**
     * 记录更新时间
     */
    @JsonIgnore
    private Date updateTime;
}
