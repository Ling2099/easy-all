package com.minio.conf;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * MinIO 客户端配置
 *
 * @author LZH
 * @version 1.0.8
 * @since 2023/07/14
 */
public class MinioConfig {

    /**
     * 服务端地址
     */
    @Value("${minio.url}")
    private String url;

    /**
     * 账号
     */
    @Value("${minio.username}")
    private String username;

    /**
     * 密码
     */
    @Value("${minio.password}")
    private String password;

    /**
     * 创建 {@link MinioClient} 对象
     *
     * @return {@link MinioClient}
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(username, password)
                .build();
    }

}
