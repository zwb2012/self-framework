package com.self.framework.common.annotion;

import com.self.framework.common.config.ParamValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author wenbo.zhuang
 * @date 2022/06/17 15:05
 **/
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ParamValidator.class)
@SuppressWarnings(value = {"unused"})
public @interface CheckRequired {
    String code() default "";

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
