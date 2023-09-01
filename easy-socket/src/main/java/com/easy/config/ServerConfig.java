package com.easy.config;

import com.easy.handler.HeartbeatHandler;
import com.easy.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Netty WebSocket 服务端配置
 *
 * @author LZH
 * @version 1.0.11
 * @since 2023-08-27
 */
@ConditionalOnProperty(name = "easy.netty-server")
public class ServerConfig {

    private static final Logger log = LoggerFactory.getLogger(ServerConfig.class);

    /**
     * 服务端端口号
     */
    @Value("${easy.netty-server-port:9999}")
    private Integer port;

    /**
     * 初始化 Netty WebSocket
     */
    @PostConstruct
    public void start() {
        // 创建两个线程组: bossGroup 接受客户端连接, workerGroup 负责网络的读写
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 使用默认的 NioEventLoop 线程（底层为 CPU 核心数 * 2: NettyRuntime.availableProcessors() * 2）
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务端的启动引导类
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 1、绑定两组线程组
            // 2、指定所使用的 NIO 传输 Channel
            // 3、设置日志等级为 INFO 的日志记录器
            // 4、设置 Channel 对应的活跃机制
            // 5、设置队列可用大小
            // 6、为 Pipeline 指定相关的 Handler（两者为相互包容, 且在同一责任链上）
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                    .option(ChannelOption.SO_BACKLOG, 65536)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(@NonNull SocketChannel channel) {
                            // 0、 获取 Pipeline 管道
                            // 1、 设置 HTTP 解编码器, 因为 WebSocket 协议本身是基于 HTTP 协议的
                            // 2、 以块的方式来写的处理器(添加对于读写大数据流的支持)
                            // 3、 Http 数据在传输过程中是分段的, HttpObjectAggregator 可以将多个段聚合（当浏览器发送大量数据时, 就会发送多次http请求）
                            // 4、 设置 WebSocket 心跳机制, 即 300s 空闲时间未收到客户端消息, 则断开连接, 减少资源浪费
                            // 5、 将心跳处理器添加至管道中
                            // 6、 自定义业务处理的 Handler
                            // 7、 WebSocket 数据压缩扩展
                            // 8、 WebSocket 服务器处理的协议（数据以帧 frame 的形式传输）, 同时将 HTTP 协议升级成为 ws 协议
                            // 9、 添加解码器
                            // 10、添加编码器
                            channel.pipeline()
                                    .addLast(new HttpServerCodec())
                                    .addLast(new ChunkedWriteHandler())
                                    .addLast(new HttpObjectAggregator(8192))
                                    .addLast(new IdleStateHandler(0, 0, 300, TimeUnit.SECONDS))
                                    .addLast(new HeartbeatHandler())
                                    .addLast(new WebSocketHandler())
                                    .addLast(new WebSocketServerCompressionHandler())
                                    .addLast(new WebSocketServerProtocolHandler(
                                            "/socket",
                                            "WebSocket",
                                            true,
                                            655360
                                    ))
                                    .addLast("decoder", new StringDecoder())
                                    .addLast("encoder", new StringDecoder());
                        }
                    });

            // 异步绑定服务器, 调用 sync() 方法阻塞等待直到绑定完成
            ChannelFuture future = bootstrap.bind(new InetSocketAddress(port)).sync();
            // 获取 Channel 的 CloseFuture, 并且阻塞当前线程直到它完成
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        } finally {
            // 优雅地停止线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
