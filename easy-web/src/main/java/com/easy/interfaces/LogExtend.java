package com.easy.interfaces;

import com.basic.exception.BaseException;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * 全局异常日志扩展接口
 *
 * @author LZH
 * @version 1.0.4
 * @since 2023-05-21
 */
public interface LogExtend {

    /**
     * 记录日志
     *
     * @param ex {@link Exception}
     */
    void log(Exception ex);

    /**
     * 记录日志
     *
     * @param ex {@link BaseException}
     */
    void log(BaseException ex);

    /**
     * 记录日志
     *
     * @param ex {@link MethodArgumentNotValidException}
     */
    void log(MethodArgumentNotValidException ex);

}
