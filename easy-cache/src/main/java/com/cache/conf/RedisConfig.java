package com.cache.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * {@link RedisTemplate} 配置类, 包含序列化与反序列化
 *
 * @author LZH
 * @version 1.0.5
 * @since 2023/05/02
 */
public class RedisConfig {

    /**
     * 配置 {@link RedisTemplate} Redis 数据访问辅助类
     *
     * @param factory {@link RedisConnectionFactory}
     * @return {@link RedisTemplate}
     */
    @Bean
    @SuppressWarnings(value = { "unchecked", "rawtypes" })
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        RedisSerializer serializer = new JsonSerializer(Object.class);

        // 使用 StringRedisSerializer 来序列化和反序列化 redis 的 key 值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Hash 的 key 也采用 StringRedisSerializer 的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
