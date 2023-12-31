package com.easy.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

/**
 * 合并 {@link RestController} 与 {@link RequestMapping} 注解
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023/05/06
 */
@Documented
@RestController
@RequestMapping
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestMapping {

    /**
     * {@link RequestMapping#path()}
     *
     * @return {@link String}
     */
    @AliasFor("path")
    String[] value() default {};

    /**
     * {@link RestController#value()}
     *
     * @return {@link String}
     */
    @AliasFor("value")
    String[] path() default {};

}
