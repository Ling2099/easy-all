package com.easy.annotations;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.*;

/**
 * 自定义服务启动注解
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023/05/06
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
// 开启异步支持（to be seen）
@EnableAsync
// Spring Boot 启动注解
@SpringBootApplication
// 使用 cglib 代理
// @EnableAspectJAutoProxy(exposeProxy = true)
public @interface StartAnnotations {

    /**
     * 排除指定的目标类
     *
     * @see SpringBootApplication#exclude()
     * @return {@link Class}
     */
    @AliasFor(annotation = SpringBootApplication.class)
    Class<?>[] exclude() default {};

    /**
     * 排除指定的目标类（使用名称）
     *
     * @see SpringBootApplication#excludeName()
     * @return {@link String}
     */
    @AliasFor(annotation = SpringBootApplication.class)
    String[] excludeName() default {};

    /**
     * 扫描包组件（使用类名）
     *
     * @see SpringBootApplication#scanBasePackages
     * @return {@link String}
     */
    @AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
    String[] scanBasePackages() default {};

    /**
     * 扫描包组件
     *
     * @see SpringBootApplication#scanBasePackageClasses
     * @return {@link Class}
     */
    @AliasFor(annotation = ComponentScan.class, attribute = "basePackageClasses")
    Class<?>[] scanBasePackageClasses() default {};

    /**
     * 自定义 Bean 名称生成规则
     *
     * @see SpringBootApplication#nameGenerator
     * @return {@link Class}
     */
    @AliasFor(annotation = ComponentScan.class, attribute = "nameGenerator")
    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

    /**
     * 是否使用代理
     *
     * @see SpringBootApplication#proxyBeanMethods
     * @return bool
     */
    @AliasFor(annotation = Configuration.class)
    boolean proxyBeanMethods() default true;

}
