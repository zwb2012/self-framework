package com.self.framework.common.config;

import com.self.framework.common.annotion.CheckRequired;
import com.self.framework.common.exception.BusinessException;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;

/**
 * @author wenbo.zhuang
 * @date 2022/06/17 15:06
 **/
public class ParamValidator implements ConstraintValidator<CheckRequired, Object> {

    String code = "";

    String message = "";

    @Override
    public void initialize(CheckRequired checkRequired) {
        //初始化的时候
        this.code = checkRequired.code();
        this.message = checkRequired.message();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value instanceof ArrayList && CollectionUtils.isEmpty((ArrayList) value)) {
            throw new BusinessException(code, message);
        }
        if (value == null) {
            throw new BusinessException(code, message);
        }
        return true;
    }
}
