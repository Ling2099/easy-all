package com.easy.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 客户端心跳处理器
 *
 * @author LZH
 * @version 1.0.11
 * @since 2023-08-27
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    /**
     * 实现 {@link #userEventTriggered(ChannelHandlerContext, Object)} 方法, 发送心跳。
     *
     * <p>当发送失败时, 关闭该连接</p>
     *
     * @param ctx        责任链上的 {@link ChannelHandlerContext} 上下文
     * @param evt        维护心跳触发时间
     * @throws Exception 当该方法内出现异常时
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 当客户端失活时, 首先推送通知消息, 然后再关闭 Channel
            ctx.writeAndFlush(MessageVo.wrap(NumEnum.NINE, NumEnum.NINE.getMsg()));
            // 关闭对应通道
            ctx.channel().close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
