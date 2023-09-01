package com.easy.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Netty WebSocket 客户端配置
 *
 * @author LZH
 * @version 1.0.11
 * @since 2023-09-01
 */
@ConditionalOnProperty(name = "easy.netty-client")
public class ClientConfig {

    // https://blog.csdn.net/CodeDabaicai/article/details/113574593

}
