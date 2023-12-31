package com.basic.tool;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 扩展集合 {@code foreach} 循环时的下标获取
 *
 * @author LZH
 * @version 1.0.7
 * @since 2023/06/15
 */
public class Iterator {

    /**
     * {@link java.lang.Iterable#forEach(Consumer)} 循环时同时获取数据与下标
     *
     * @param consumer {@link BiConsumer}
     * @param <T>      泛型
     * @return {@link Consumer}
     */
    public static <T> Consumer<T> each(BiConsumer<Integer, T> consumer) {
        class Count {
            int index;
        }
        Count count = new Count();
        return item -> consumer.accept(count.index++, item);
    }

}
