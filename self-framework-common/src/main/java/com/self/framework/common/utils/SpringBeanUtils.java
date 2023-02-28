package com.self.framework.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * SpringBean工具类
 *
 * @author wenbo.zhuang
 * @date 2022/07/08 11:23
 **/
@SuppressWarnings("all")
public class SpringBeanUtils implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

    private static ApplicationContext applicationContext;
    private static DefaultListableBeanFactory beanFactory;
    private static BeanDefinitionRegistry beanDefinitionRegistry;
    private static BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry definitionRegistry) throws BeansException {
        beanDefinitionRegistry = definitionRegistry;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }

    public static String buildBean(BeanDefinition definition) {
        String beanName = beanNameGenerator.generateBeanName(definition, beanDefinitionRegistry);
        buildBean(beanName, definition);
        return beanName;
    }

    public static String buildBean(String beanName, BeanDefinition definition) {
        beanDefinitionRegistry.registerBeanDefinition(beanName, definition);
        return beanName;
    }
}
