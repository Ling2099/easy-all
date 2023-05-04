package com.orm.conf;

import cn.hutool.core.map.MapUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地线程副本的封装, <b style='color:red'>适用于用户状态数据</b>
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-04
 */
public final class ThreadLocalConfig {

    /**
     * 线程私有内存数据
     */
    private static final ThreadLocal<Map<String, Object>> USER_DATA = new ThreadLocal<>();

    /**
     * 获取 ThreadLocal 值
     *
     * @param key 键
     * @param <T> 泛型
     * @return T
     */
    public static <T> T get(String key) {
        // noinspection unchecked
        return (T) initial().get(key);
    }

    /**
     * 设置 ThreadLocal 值
     *
     * @param key   键
     * @param value 值
     * @param <T>   泛型
     */
    public static <T> void set(String key, T value) {
        initial().put(key, value);
    }

    /**
     * 移除当前线程的副本数据
     */
    public static void remove() {
        USER_DATA.remove();
    }

    /**
     * 初始化或获取 ThreadLocal 中的值
     *
     * @return {@link Map}
     */
    private static Map<String, Object> initial() {
        Map<String, Object> map = USER_DATA.get();
        if (MapUtil.isEmpty(map)) {
            USER_DATA.set(map = new ConcurrentHashMap<>(32));
        }
        return map;
    }
}
