package com.easy.config;

import com.basic.domain.ResultVo;
import com.basic.exception.BaseException;
import com.easy.interfaces.LogExtend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 *
 * <ol>
 *     <li>{@link Exception} 系统异常</li>
 *     <li>{@link BaseException} 自定义基础异常</li>
 *     <li>{@link MethodArgumentNotValidException} 参数校验异常</li>
 * </ol>
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-19
 */
@RestControllerAdvice
@ConditionalOnProperty(name = "easy.exception")
public class GlobalException extends RuntimeException {

    public static final Logger log = LoggerFactory.getLogger(GlobalException.class);

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = false)
    private LogExtend logExtend;

    /**
     * 全局异常 {@link Exception} 捕获
     *
     * @param response {@link HttpServletResponse}
     * @param ex       {@link Exception}
     * @return {@link ResultVo}
     */
    @ExceptionHandler(Exception.class)
    public ResultVo<?> globalHandle(HttpServletResponse response, Exception ex) {
        // 设置 HTTP 响应码
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        if (logExtend != null) {
            logExtend.log(ex);
        }

        log.error(ex.getMessage(), ex);
        return ResultVo.fail();
    }

    /**
     * 全局异常 {@link BaseException} 捕获
     *
     * @param response {@link HttpServletResponse}
     * @param ex       {@link BaseException}
     * @return {@link ResultVo}
     */
    @ExceptionHandler(BaseException.class)
    public ResultVo<?> baseHandle(HttpServletResponse response, BaseException ex) {
        // 设置 HTTP 响应码
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        if (logExtend != null) {
            logExtend.log(ex);
        }

        log.error(ex.getMsg(), ex);
        return StringUtils.hasLength(ex.getMsg()) ? ResultVo.fail() : ResultVo.fail(ex.getMsg());
    }

    /**
     * 全局异常 {@link MethodArgumentNotValidException} 捕获
     *
     * @param response {@link HttpServletResponse}
     * @param ex       {@link MethodArgumentNotValidException}
     * @return {@link ResultVo}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVo<?> argsHandle(HttpServletResponse response, MethodArgumentNotValidException ex) {
        BindingResult binding = ex.getBindingResult();
        FieldError error = binding.getFieldError();
        assert error != null;
        // 设置 HTTP 响应码
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        if (logExtend != null) {
            logExtend.log(ex);
        }

        log.error(error.getDefaultMessage(), ex);
        return ResultVo.fail(error.getDefaultMessage());
    }

}
