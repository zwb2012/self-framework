package com.self.framework.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * description:
 *
 * @author wenbo.zhuang
 * @date 2022/02/17 18:57
 **/

@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtils.applicationContext == null) {
            SpringContextUtils.applicationContext = applicationContext;
        }

    }

    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    public static Object getBean(String name) {
        checkApplicationContext();
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        checkApplicationContext();
        return getApplicationContext().getBean(name, clazz);
    }

    public static void cleanApplicationContext() {
        checkApplicationContext();
        applicationContext = null;
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
        }
    }
}

