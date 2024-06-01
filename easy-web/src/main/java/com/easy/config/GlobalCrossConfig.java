package com.easy.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Collections;

/**
 * 全局跨于设置
 *
 * @author LZH
 * @version 1.0.9
 * @since 2023/07/21
 */
public class GlobalCrossConfig {

    /**
     * 跨域配置
     *
     * <p>reactive 版本</p>
     *
     * @return {@link CorsWebFilter}
     */
    @Bean
    @ConditionalOnProperty(name = "easy.cross-web")
    public CorsWebFilter crossFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

    /**
     * 跨域配置
     *
     * <p>SpringBoot 2.7.11 版本</p>
     *
     * @return {@link CorsFilter}
     * @since 1.0.12
     */
    @Bean
    @ConditionalOnProperty(name = "easy.cross")
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        corsConfiguration.setAllowCredentials(true);
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

}
