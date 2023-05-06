package com.easy.advice;

import com.basic.domain.ResultVo;
import com.basic.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 自定义全局异常处理
 *
 * @author LZH
 * @version 5.0.0
 * @since 2023-05-06
 */
@ConditionalOnProperty(name = "easy.exception")
public class GlobalException implements ErrorWebExceptionHandler {

    public static final Logger log = LoggerFactory.getLogger(GlobalException.class);

    /**
     * 全局异常处理具体实现
     *
     * @param exchange {@link ServerWebExchange}
     * @param ex       {@link Throwable}
     * @return {@link Mono}
     */
    @NonNull
    @Override
    public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
        // 设置响应头及响应状态码
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 包裹自定义响应结果
        DataBuffer wrap = response.bufferFactory().wrap(this.result(ex).toString().getBytes());
        // 日志打印
        log.error("Application error: {}", ex.getMessage(), ex);

        // 返回 Mono
        return response.writeWith(Mono.just(wrap));
    }

    /**
     * 获取异常时的返回结果 {@link ResultVo}
     *
     * @param ex 当前抛出的异常 {@link Throwable}
     * @return {@link ResultVo} 客户端响应结果
     */
    private ResultVo<?> result(Throwable ex) {
        return switch (ex) {
            // 当为自定义异常时
            case BaseException business -> ResultVo.fail(business.getMsg());
            // 当为全局参数异常时
            case MethodArgumentNotValidException arg ->
                    ResultVo.fail(
                            Objects.requireNonNull(arg.getBindingResult().getFieldError()).getDefaultMessage()
                    );
            // 默认响应结果
            default -> ResultVo.fail();
        };
    }
}
