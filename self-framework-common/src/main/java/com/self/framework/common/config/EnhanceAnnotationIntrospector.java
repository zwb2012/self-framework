package com.self.framework.common.config;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import lombok.val;

import java.lang.annotation.Annotation;

/**
 * @author wenbo.zhuang
 * @date 2022/06/16 17:53
 **/
public class EnhanceAnnotationIntrospector extends JacksonAnnotationIntrospector {
    private static final long serialVersionUID = 6279908897302814638L;

    @Override
    public boolean isAnnotationBundle(Annotation ann) {
        return JSONField.class == ann.annotationType() || super.isAnnotationBundle(ann);
    }

    @Override
    public PropertyName findNameForSerialization(Annotated ann) {
        val name = super.findNameForSerialization(ann);
        if (name == null || name == PropertyName.USE_DEFAULT) {
            val jsonField = _findAnnotation(ann, JSONField.class);
            if (jsonField != null) {
                return PropertyName.construct(jsonField.name());
            }
        }
        return name;
    }

    @Override
    public PropertyName findNameForDeserialization(Annotated ann) {
        val name = super.findNameForDeserialization(ann);
        if (name == null || name == PropertyName.USE_DEFAULT) {
            val jsonField = _findAnnotation(ann, JSONField.class);
            if (jsonField != null) {
                return PropertyName.construct(jsonField.name());
            }
        }
        return name;
    }
}
