package com.easy.filter;

import com.orm.tool.ThreadLocalTool;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * TODO
 *
 * @author LZH
 * @version 5.0.0
 * @since 2023-05-06
 */
public class HeaderFilter implements WebFilter {

    @NotNull
    @Override
    public Mono<Void> filter(@NotNull ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getRequest()
                .getHeaders()
                .toSingleValueMap()
                .forEach(ThreadLocalTool::set);
        return chain.filter(exchange);
    }


//    @Bean
//    public WebFilter contextPathWebFilter() {
//        String contextPath = serverProperties.getServlet().getContextPath();
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            if (request.getURI().getPath().startsWith(contextPath)) {
//                return chain.filter(
//                        exchange.mutate()
//                                .request(request.mutate().contextPath(contextPath).build())
//                                .build());
//            }
//            return chain.filter(exchange);
//        };
//    }
}
