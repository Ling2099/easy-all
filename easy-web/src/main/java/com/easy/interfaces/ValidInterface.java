package com.easy.interfaces;

/**
 * 接口参数的校验注解
 *
 * @author LZH
 * @version 1.0.11
 * @since 2023/12/30
 */
public interface ValidInterface {

    /**
     * 具体执行参数校验的逻辑接口
     *
     * @param params 目标方法入参参数
     * @param <T>    泛型
     */
    @SuppressWarnings("unchecked")
    <T> void handle(T...params);

}
