package com.easy.annotations;

import com.easy.interfaces.ValidInterface;

import java.lang.annotation.*;

/**
 * 自定义参数校验注解
 *
 * @author LZH
 * @version 1.0.11
 * @since 2023/12/30
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validation {

    /**
     * 必填, 实现于 {@link ValidInterface} 接口的自定义参数校验逻辑类; 同时需要注入 Spring 容器中
     *
     * @return {@link ValidInterface}
     */
    Class<? extends ValidInterface> value();

}
