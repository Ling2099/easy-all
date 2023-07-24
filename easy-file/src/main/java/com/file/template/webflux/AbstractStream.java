package com.file.template.webflux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.function.Consumer;

/**
 * WebFlux 相关操作类
 *
 * @author LZH
 * @version 1.0.9
 * @since 2023-07-23
 */
public abstract class AbstractStream {

    private static final Logger log = LoggerFactory.getLogger(AbstractStream.class);

    /**
     * 调用程序
     *
     * @param fileName 文件名
     * @param response {@link ServerHttpResponse}
     * @param consumer {@link Consumer}
     * @return {@link Mono}
     */
    protected static Mono<Void> invoker(String fileName, ServerHttpResponse response,
                                        Consumer<OutputStream> consumer) {
        setHeader(fileName, response);

        DefaultDataBuffer dataBuffer = new DefaultDataBufferFactory().allocateBuffer();
        OutputStream os = dataBuffer.asOutputStream();

        consumer.accept(os);

        Flux<DataBuffer> flux = Flux.create((FluxSink<DataBuffer> emitter) -> {
            emitter.next(dataBuffer);
            emitter.complete();
        });

        return response.writeWith(flux);
    }

    /**
     * 设置响应头信息
     *
     * @param fileName 导出的文件名
     * @param response {@link ServerHttpResponse}
     */
    private static void setHeader(String fileName, ServerHttpResponse response) {
        try {
            fileName = String.format("attachment; filename=%s", URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("String Encode: ", e);
        }

        response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, fileName);
        response.getHeaders().add("Accept-Ranges", "bytes");
    }

}
