package com.self.framework.common.config;//package com.zwb.yx.server.common.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.Collections;
//
///**
// * description: 全局跨域配置
// *
// * @author wenbo.zhuang
// * @date 2022/03/14 23:17
// **/
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@Configuration
//public class GlobalCorsConfig {
//
//    /**
//     * 允许跨域调用的过滤器
//     */
//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        //允许所有域名进行跨域调用
//        config.setAllowedOriginPatterns(Collections.singletonList("*"));
//        //允许跨越发送cookie， 允许凭证
//        config.setAllowCredentials(true);
//        //允许任何请求头
//        config.addAllowedHeader(CorsConfiguration.ALL);
//        //允许所有请求方法跨域调用
//        config.addAllowedMethod(CorsConfiguration.ALL);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//}
