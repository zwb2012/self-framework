package com.self.framework.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.ArrayList;
import java.util.List;


/**
 * description: 用于配置白名单资源路径
 *
 * @author wenbo.zhuang
 * @date 2021/05/12 10:47
 **/
@Getter
@Setter
@EnableConfigurationProperties(IgnoreUrlsConfig.class)
@ConfigurationProperties(prefix = "secure.ignored")
public class IgnoreUrlsConfig {

    private List<String> urls = new ArrayList<>();
}
