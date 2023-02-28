package com.self.framework.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.self.framework.common.config.JsonSerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * bean克隆
 *
 * @author wenbo.zhuang
 * @date 2022/06/16 17:51
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanCloneUtils {

    public static <T> T clone(Object src, JavaType type) {
        return src != null && type != null ? JsonSerializer.convertValue(src, type) : null;
    }

    public static void main(String[] args) {
        Object a = new Object();
        // 但对象复制
        Object clone = BeanCloneUtils.clone(a, TypeFactory.defaultInstance().constructType(Object.class));
        log.info(JSON.toJSONString(clone));
        // 集合复制
        clone(Collections.singleton(a), JsonSerializer.toJavaType(new TypeReference<List<Object>>() {}.getType()));
    }
}
