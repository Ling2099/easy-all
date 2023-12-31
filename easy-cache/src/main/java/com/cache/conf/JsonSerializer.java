package com.cache.conf;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.nio.charset.StandardCharsets;

/**
 * 配置 Redis FastJson 序列化
 *
 * @author LZH
 * @version 1.0.5
 * @since 2023/06/01
 */
public class JsonSerializer<T> implements RedisSerializer<T> {

    private final Class<T> clazz;

    /**
     * 构造函数
     *
     * @param clazz {@link Class}
     */
    public JsonSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    /**
     * 序列化
     *
     * @param t T
     * @return byte[]
     */
    @Override
    public byte[] serialize(T t) {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName)
                .getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 反序列化
     *
     * @param bytes byte[]
     * @return T
     */
    @Override
    public T deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String str = new String(bytes, StandardCharsets.UTF_8);

        return JSON.parseObject(str, clazz, JSONReader.Feature.SupportAutoType);
    }

}
