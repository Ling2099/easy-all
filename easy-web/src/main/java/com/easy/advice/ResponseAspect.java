package com.easy.advice;

import com.basic.domain.ResultVo;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * WebFlux 返回结果转换 代理类
 *
 * @author LZH
 * @version 5.0.0
 * @since 2023-05-06
 */
@Aspect
@ConditionalOnProperty(name = "easy.res")
public class ResponseAspect {

    /**
     * WebFlux 返回结果转换
     *
     * <p>
     *     将控制层的 {@link ResultVo} 响应结果包裹至 {@link Mono},
     *     同时根据程序运行的状态结果 {@link ResultVo#getCode()} 设置至 HTTP 响应结果中
     * </p>
     *
     * @param exchange {@link ServerWebExchange}
     * @param result   {@link HandlerResult}
     * @return {@link Mono}
     */
    @AfterReturning(
            value = "execution(* org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler.handleResult(..)) && args(exchange, result)",
            argNames = "exchange,result"
    )
    public Mono<ResultVo<?>> handle(ServerWebExchange exchange, HandlerResult result) {
        // 获取自定义响应结果类
        ResultVo<?> vo = (ResultVo<?>) result.getReturnValue();
        Integer code = Optional.ofNullable(vo)
                .map(ResultVo::getCode)
                .orElse(500);

        // 设置 HTTP 响应状态
        HttpStatus status = HttpStatus.valueOf(code);
        exchange.getResponse().setStatusCode(status);

        // 返回结果
        assert vo != null;
        return Mono.just(vo);
    }

}
