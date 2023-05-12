package com.easy.filter;

import com.orm.tool.ThreadLocalTool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 可选的 web 过滤器
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-06
 */
@Order(300)
@ConditionalOnProperty(name = "easy.filter")
public class HeaderFilter implements WebFilter {

    /**
     * 请求头设置至本地线程
     *
     * <p style="color:red">WebFlux 中不推荐使用 ThreadLocal, 后续版本会对此做调整</p>
     *
     * @param exchange {@link ServerWebExchange}
     * @param chain    {@link WebFilterChain}
     * @return {@link Mono}
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getRequest()
                .getHeaders()
                .toSingleValueMap()
                .forEach(ThreadLocalTool::set);
        return chain.filter(exchange);
    }
}
