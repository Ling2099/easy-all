package com.easy.config;

import com.easy.filter.HeaderFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义拦截器配置
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-19
 */
@ConditionalOnProperty(name = "easy.filter")
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 排除在拦截器外的地址
     */
    @Value("easy.url:#{null}")
    private String[] exclusionUrl;

    /**
     * 自定义消息头拦截器
     *
     * @param registry {@link InterceptorRegistry}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HeaderFilter())
                .addPathPatterns("/**")
                .excludePathPatterns(exclusionUrl)
                .order(-10);
    }

}
