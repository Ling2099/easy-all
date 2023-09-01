package com.easy.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义 Server Handler 处理器
 *
 * <ol>
 *     <li>建立连接</li>
 *     <li>消息接受</li>
 *     <li>消息推送</li>
 *     <li>关闭连接</li>
 *     <li>连接异常</li>
 * </ol>
 *
 * @author LZH
 * @version 1.0.11
 * @since 2023-09-01
 */
public class ServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    /**
     * 定义 Channel 组, 用以管理客户端所请求过来的所有 Channel
     */
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 创建用于保存 Channel 的集合
     *
     * <ul>
     *     <li>k: 客户端请求消息头自定义</li>
     *     <li>v: 客户端当前 {@link Channel}</li>
     * </ul>
     */
    private static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * 读取客户端消息
     *
     * @param context {@link ChannelHandlerContext}
     * @param msg     {@link TextWebSocketFrame}
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, TextWebSocketFrame msg) {
        Channel channel = context.channel();
        for (Channel item : CHANNEL_GROUP) {
            // 不是当前的 Channel 时, 直接转发
            if (!item.equals(channel)) {
                item.writeAndFlush(new TextWebSocketFrame(msg.text()));
            }
        }
    }

    /**
     * 当客户端连接后, 所调用的方法
     *
     * @param context {@link ChannelHandlerContext}
     * @param msg     客户端请求对象
     * @throws Exception 当发生异常时
     */
    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        // 当客户端发送消息时, 不执行
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String id = request.headers().get("id");
            CHANNEL_MAP.put(id, context.channel());
        }
        super.channelRead(context, msg);
    }

    /**
     * 当 Channel 断开连接时
     *
     * <P>推送给其它在线的 Channel</P>
     *
     * @param context {@link ChannelHandlerContext}
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext context) {
        Channel channel = context.channel();
        // 移除散列表和通道组中对应的 Channel
        if (CHANNEL_MAP.containsValue(channel)) {
            CHANNEL_MAP.values().remove(channel);
            CHANNEL_GROUP.remove(channel);
        }
    }

    /**
     * 当发生异常时, 记录日志, 并关闭通道
     *
     * @param context {@link ChannelHandlerContext}
     * @param cause   {@link Throwable}
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        log.error("Netty has an Exception", cause);
        context.close();
    }
}
