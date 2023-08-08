package com.basic.tool;

/**
 * Pair 扩展
 *
 * @author LZH
 * @version 1.0.7
 * @since 2023-06-15
 */
public class Trine<T, K, V> {

    /**
     * T of this Trine
     */
    private T t;

    /**
     * K of this Trine
     */
    private K k;

    /**
     * V of this Trine
     */
    private V v;

    /**
     * 私有无参构造函数
     */
    private Trine() {}

    /**
     * 私有全参构造函数
     *
     * @param t {@link #t}
     * @param k {@link #k}
     * @param v {@link #v}
     */
    private Trine(T t, K k, V v) {
        this.t = t;
        this.k = k;
        this.v = v;
    }

    /**
     * 创建一个新的 {@link Trine}
     *
     * @param t   {@link #t}
     * @param k   {@link #k}
     * @param v   {@link #v}
     * @param <T> 泛型
     * @param <K> 泛型
     * @param <V> 泛型
     * @return {@link Trine}
     */
    public static <T, K, V> Trine<T, K, V> of(T t, K k, V v) {
        return new Trine<>(t, k, v);
    }

    /**
     * Gets the n for this Trine.
     *
     * @return {@link #t}
     */
    public T getT() {
        return t;
    }

    /**
     * Gets the k for this Trine.
     *
     * @return {@link #k}
     */
    public K getK() {
        return k;
    }

    /**
     * Gets the v for this Trine.
     *
     * @return {@link #v}
     */
    public V getV() {
        return v;
    }
}
