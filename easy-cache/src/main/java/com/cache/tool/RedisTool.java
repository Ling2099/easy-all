package com.cache.tool;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.util.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 简单封装 Redis 工具类
 *
 * <ol>
 *     <li>
 *         key 相关操作
 *         <ul>
 *             <li>{@link #hasKey(String)}: 校验 key 是否存在</li>
 *             <li>{@link #keys(String)}: 获取缓存的基本对象列表</li>
 *         </ul>
 *     </li>
 *     <li>
 *         过期时间相关
 *         <ul>
 *             <li>{@link #getExpire(String)}: 获取指定 key 的剩余时间</li>
 *             <li>{@link #expire(String, long)}: 对指定的数据设置过期时间, 以秒为单位</li>
 *             <li>{@link #expire(String, long, TimeUnit)}: 对指定的数据设置过期时间</li>
 *         </ul>
 *     </li>
 *     <li>
 *         递增递减
 *         <ul>
 *             <li>{@link #incr(String)}: 原子性递增</li>
 *             <li>{@link #decr(String)}: 原子性递减</li>
 *         </ul>
 *     </li>
 *     <li>
 *         字符串(String)
 *         <ul>
 *             <li>{@link #get(String)}: 通过指定 key 获取 Redis 缓存数据</li>
 *             <li>{@link #set(String, Object)}: 缓存基本数据</li>
 *             <li>{@link #set(String, Object, Long)}: 缓存基本数据, 以秒为单位</li>
 *             <li>{@link #set(String, Object, Long, TimeUnit)}: 缓存基本数据</li>
 *         </ul>
 *     </li>
 *     <li>
 *         列表(List)
 *         <ul>
 *             <li>{@link #setLeft(String, Object)}: 将数据插入列表的头部</li>
 *             <li>{@link #setLeft(String, Object[])}: 将数据插入列表的头部</li>
 *             <li>{@link #setLeft(String, List)}: 将集合全部插入列表的头部</li>
 *             <li>{@link #getLeft(String)}: 删除并返回列表中的第一个头部的元素</li>
 *             <li>{@link #getLeft(String, long)}: 删除并返回列表中从头部开始的多个元素</li>
 *             <li>{@link #getLeft(String, long, TimeUnit)}: 删除并返回列表中的第一个头部的元素</li>
 *             <li>{@link #setRight(String, Object)}: 将数据插入列表的尾部</li>
 *             <li>{@link #setRight(String, Object[])}: 将数据插入列表的尾部</li>
 *             <li>{@link #setRight(String, List)}: 将集合全部插入列表的尾部</li>
 *             <li>{@link #getRight(String)}: 删除并返回列表中的第一个尾部的元素</li>
 *             <li>{@link #getRight(String, long)}: 删除并返回列表中从尾部部开始的多个元素</li>
 *             <li>{@link #getRight(String, long, TimeUnit)}: 删除并返回列表中的第一个尾部的元素</li>
 *         </ul>
 *     </li>
 *     <li>
 *         集合(Set)
 *         <ul>
 *             <li>{@link #addSet(String, Object[])}: 添加元素至集合中</li>
 *             <li>{@link #getMembers(String)}: 获取 key 中的所有值</li>
 *             <li>{@link #getSize(String)}: 获取 key 对应的集合长度</li>
 *             <li>{@link #random(String)}: 随即获取 key 对应的集合元素</li>
 *             <li>{@link #random(String, long)}: 随即获取 key 对应集合中指定个数的元素</li>
 *             <li>{@link #hasMember(String, Object)}: 判断 key 对应的集合中是否包含指定元素</li>
 *             <li>{@link #remove(String, Object[])}: 批量移除 key 对应集合集合中指定的元素</li>
 *             <li>{@link #move(String, Object, String)}: 将 key1 对应集合中的元素移动至 key2 对应的集合中</li>
 *             <li>{@link #pop(String)}: 随机弹出 key 对应集合中的元素</li>
 *             <li>{@link #pop(String, long)}: 随即弹出 key 对应集合中指定个数的元素</li>
 *             <li>{@link #difference(String, String)}: 获取 key1 与另一个 key2 集合之间的差值</li>
 *             <li>{@link #intersect(String, String)}: 获取 key1 对应集合与 key2 对应集合的交集元素</li>
 *             <li>{@link #intersect(String, Collection)}: 获取多个 key 对应集合的交集元素</li>
 *             <li>{@link #union(String, String)}: 获取两个 key 集合的合集, 并且去重</li>
 *             <li></li>
 *         </ul>
 *     </li>
 *     <li>
 *         有序集合(ZSet)
 *         <ul>
 *             <li>{@link #addZSet(String, Object, double)}: 添加元素</li>
 *             <li>{@link #drop(String, Object[])}: 删除指定 key 对应的元素值</li>
 *             <li>{@link #addScore(String, Object, double)}: 增加指定 key 元素对应 value 的 score 评分值</li>
 *             <li>{@link #rank(String, Object)}: 返回指定 key 集合中元素为 value 索引</li>
 *             <li>{@link #reverseRank(String, Object)}: 当评分从高到低时, 返回排序集中具有值的元素的索引</li>
 *             <li>{@link #range(String, long, long)}: 获取指定集合中给定区间（下标）的元素集合</li>
 *             <li>{@link #reverseRange(String, long, long)}: 获取指定集合中给定区间（下标）的元素集合</li>
 *             <li>{@link #rangeByScore(String, double, double)}: 获取按照给定 score 值范围的集合</li>
 *             <li>{@link #reverseByScore(String, double, double)}: 获取按照给定 score 值范围的集合</li>
 *             <li>{@link #reverseRangeByScore(String, double, double, long, long)}: 获取指定集合按 score 从高到底指定 score 与下标范围内的元素</li>
 *             <li>{@link #reverseRangeByScore(String, double, double, long, long)}: 获取指定集合按 score 从高到底指定 score 与下标范围内的元素</li>
 *             <li>{@link #count(String, double, double)}: 获取指定集合的 score 范围内的元素数量</li>
 *             <li>{@link #size(String)}: 获取指定集合的大小（元素个数）</li>
 *             <li>{@link #score(String, Object)}: 获取集合中 key、value 元素对应的 score 值</li>
 *             <li>{@link #removeRange(String, long, long)}: 删除指定索引区间范围内的所有元素</li>
 *             <li>{@link #removeByScore(String, double, double)}: 移除指定 score 范围的集合成员</li>
 *             <li>{@link #unionStore(String, String, String)}: 获取 key 和 key1 的并集并存储在 key2 中</li>
 *             <li>{@link #intersectStore(String, String, String)}: 获取 key 和 key1 的交集并存储在 key2 中</li>
 *             <li>{@link #scan(String)}: 获取指定集合的 value、score 值</li>
 *             <li></li>
 *         </ul>
 *     </li>
 *     <li>
 *         哈希表(Hash)
 *         <ul>
 *             <li>{@link #put(String, Object, Object)}: 单个添加 hashKey - value 键值对</li>
 *             <li>{@link #putAll(String, Map)}: 批量添加 hashKey -  value 键值对</li>
 *             <li>{@link #putIfAbsent(String, Object, Object)}: 仅当 hashKey 不存在时才添加</li>
 *             <li>{@link #entries(String)}: 获取 key 对应的键值对</li>
 *             <li>{@link #get(String, Object)}: 获取指定 {@link Map} 键是否有值,如果存在则获取值，没有则返回 {@code null}</li>
 *             <li>{@link #incr(String, Object, long)}: 将哈希 {@code hashKey} 的值增加给定的 {@code delta} 增量</li>
 *             <li>{@link #incr(String, Object, double)}: 将哈希 {@code hashKey} 的值增加给定的 {@code delta} 增量</li>
 *             <li>{@link #delete(String, Object[])}: 删除指定 {@code key} 中一个或者多个 {@code hashKeys}</li>
 *             <li>{@link #hasKey(String, Object)}: 判断指定 {@code key} 中是否有指定的 {@code hashKeys} 键</li>
 *             <li>{@link #getKeys(String)}: 获指定 {@code key} 中所有键</li>
 *             <li>{@link #values(String)}: 获指定 {@code key} 中所有值</li>
 *             <li>{@link #numbers(String)}: 获取指定 Hash 表中键的数量</li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
@SuppressWarnings(value = {"unchecked", "ClassCanBeRecord", "ConstantConditions", "ConfusingArgumentToVarargsMethod"})
public class RedisTool {

    private final RedisTemplate<String, Object> template;

    public RedisTool(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    /**
     * 校验 key 是否存在
     *
     * @param key 键
     * @return true or false
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    /**
     * 获取缓存的基本对象列表
     *
     * <p>可在 {@code pattern} 后追加 '*' 字符以匹配多个或全部键</p>
     *
     * @param pattern 模糊匹配的键
     * @return 匹配到的键集合
     */
    public Collection<String> keys(String pattern) {
        return template.keys(pattern);
    }

    /**
     * 获取指定 key 的剩余时间
     *
     * @param key 键
     * @return 剩余时间, 单位秒
     */
    public Long getExpire(String key) {
        return template.getExpire(key);
    }

    /**
     * 对指定的数据设置过期时间, <b style="color:red">以秒为单位</b>
     *
     * @param key     键
     * @param timeout 过期时间
     * @return true or false
     */
    public Boolean expire(String key, long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 对指定的数据设置过期时间
     *
     * @param key     键
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return true or false
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return template.expire(key, timeout, unit);
    }

    /**
     * 原子性递增
     *
     * <ol>
     *     <li>INCR 命令具备了 INCR AND GET 的原子操作, 即增加并返回结果的原子操作</li>
     *     <li>Redis 是单进程单线程架构, INCR 命令不会出现重复值</li>
     * </ol>
     *
     * @param key 键
     * @return 计算后的结果
     */
    public Long incr(String key) {
        return template.opsForValue().increment(key);
    }

    /**
     * 原子性递减
     *
     * @param key 键
     * @return 计算后的结果
     */
    public Long decr(String key) {
        return template.opsForValue().decrement(key);
    }

    /**
     * 通过指定 key 获取 Redis 缓存数据
     *
     * @param key 键
     * @param <T> 泛型
     * @return T 缓存数据
     */
    public <T> T get(String key) {
        return (T) template.opsForValue().get(key);
    }

    /**
     * 缓存基本数据
     *
     * @param key   键
     * @param value 数据
     * @param <T>   泛型
     */
    public <T> void set(String key, T value) {
        template.opsForValue().set(key, value);
    }

    /**
     * 缓存基本数据, <b style="color:red">以秒为单位</b>
     *
     * @param key     键
     * @param value   数据
     * @param timeout 过期时间
     * @param <T>     泛型
     */
    public <T> void set(String key, T value, Long timeout) {
        set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 缓存基本数据
     *
     * @param key      键
     * @param value    数据
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @param <T> 泛型
     */
    public <T> void set(String key, T value, Long timeout, TimeUnit timeUnit) {
        template.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 将数据插入列表的<b style="color:red">头部</b>
     *
     * @param key   键
     * @param value 数据
     * @param <T>   泛型
     * @return 插入后的数组长度
     */
    public <T> Long setLeft(String key, T value) {
        return template.opsForList().leftPush(key, value);
    }

    /**
     * 将数据插入列表的<b style="color:red">头部</b>
     *
     * @param key    键
     * @param values 数据
     * @param <T>    泛型
     * @return 插入后的数组长度
     */
    public <T> Long setLeft(String key, T... values) {
        return template.opsForList().leftPushAll(key, values);
    }

    /**
     * 将集合全部插入列表的<b style="color:red">头部</b>
     *
     * @param key  键
     * @param list 数据集合
     * @param <T>  泛型
     * @return 插入后的数组长度
     */
    public <T> Long setLeft(String key, List<T> list) {
        return template.opsForList().leftPushAll(key, list);
    }

    /**
     * 删除并返回列表中的第一个<b style="color:red">头部</b>的元素
     *
     * @param key 键
     * @param <T> 泛型
     * @return 数据元素
     */
    public <T> T getLeft(String key) {
        return (T) template.opsForList().leftPop(key);
    }

    /**
     * 删除并返回列表中从<b style="color:red">头部</b>开始的<b style="color:red">多个</b>元素
     *
     * <p style="color:yellow">注意: Redis 版本小于 6.2 时不支持该命令（语法）</p>
     *
     * @param key   键
     * @param count 需要获取的元素个数
     * @param <T>   泛型
     * @return 集合对象
     */
    public <T> List<T> getLeft(String key, long count) {
        return (List<T>) template.opsForList().leftPop(key, count);
    }

    /**
     * 删除并返回列表中的第一个<b style="color:red">头部</b>的元素
     *
     * <p style="color:yellow">如果这个元素不存在, 则阻塞等待, 其最长的阻塞时间为当前设置的超时时间 {@code unit}</p>
     *
     * @param key     键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @param <T>     泛型
     * @return 数据元素
     */
    public <T> T getLeft(String key, long timeout, TimeUnit unit) {
        return (T) template.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 将数据插入列表的<b style="color:red">尾部</b>
     *
     * @param key   键
     * @param value 数据
     * @param <T>   泛型
     * @return 插入后的数组长度
     */
    public <T> Long setRight(String key, T value) {
        return template.opsForList().rightPush(key, value);
    }

    /**
     * 将数据插入列表的<b style="color:red">尾部</b>
     *
     * @param key   键
     * @param values 数据
     * @param <T>   泛型
     * @return 插入后的数组长度
     */
    public <T> Long setRight(String key, T... values) {
        return template.opsForList().rightPushAll(key, values);
    }

    /**
     * 将集合全部插入列表的<b style="color:red">尾部</b>
     *
     * @param key  键
     * @param list 数据集合
     * @param <T>  泛型
     * @return 插入后的数组长度
     */
    public <T> Long setRight(String key, List<T> list) {
        return template.opsForList().rightPushAll(key, list);
    }

    /**
     * 删除并返回列表中的第一个<b style="color:red">尾部</b>的元素
     *
     * @param key 键
     * @param <T> 泛型
     * @return 数据元素
     */
    public <T> T getRight(String key) {
        return (T) template.opsForList().rightPop(key);
    }

    /**
     * 删除并返回列表中从<b style="color:red">尾部部</b>开始的<b style="color:red">多个</b>元素
     *
     * <p style="color:yellow">注意: Redis 版本小于 6.2 时不支持该命令（语法）</p>
     *
     * @param key   键
     * @param count 需要获取的元素个数
     * @param <T>   泛型
     * @return 集合对象
     */
    public <T> List<T> getRight(String key, long count) {
        return (List<T>) template.opsForList().rightPop(key, count);
    }

    /**
     * 删除并返回列表中的第一个<b style="color:red">尾部</b>的元素
     *
     * <p style="color:yellow">如果这个元素不存在, 则阻塞等待, 其最长的阻塞时间为当前设置的超时时间 {@code unit}</p>
     *
     * @param key     键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @param <T>     泛型
     * @return 数据元素
     */
    public <T> T getRight(String key, long timeout, TimeUnit unit) {
        return (T) template.opsForList().rightPop(key, timeout, unit);
    }

    /**
     * 添加元素至集合中
     *
     * @param key 键
     * @param ts  元素数据
     * @param <T> 泛型
     * @return 插入后的数组长度
     * @since 1.0.5
     */
    public <T> Long addSet(String key, T... ts) {
        return template.opsForSet().add(key, ts);
    }

    /**
     * 获取 key 中的所有值
     *
     * @param key 键
     * @param <T> 泛型
     * @return {@link Set} 集合
     * @since 1.0.5
     */
    public <T> Set<T> getMembers(String key) {
        return (Set<T>) template.opsForSet().members(key);
    }

    /**
     * 获取 key 对应的集合长度
     *
     * @param key 键
     * @return 集合长度
     * @since 1.0.5
     */
    public Long getSize(String key) {
        return template.opsForSet().size(key);
    }

    /**
     * 随即获取 key 对应的集合元素
     *
     * @param key 键
     * @param <T> 泛型
     * @return 随即出来的元素
     * @since 1.0.5
     */
    public <T> T random(String key) {
        return (T) template.opsForSet().randomMember(key);
    }

    /**
     * 随即获取 key 对应集合中指定个数（{@code num}）的元素
     *
     * @param key 键
     * @param num 指定获取的元素个数
     * @param <T> 泛型
     * @return 获取的元素集合
     * @since 1.0.5
     */
    public <T> List<T> random(String key, long num) {
        return (List<T>) template.opsForSet().randomMembers(key, num);
    }

    /**
     * 判断 key 对应的集合中是否包含指定（{@code t}）元素
     *
     * @param key 键
     * @param t   指定元素
     * @param <T> 泛型
     * @return true/false
     * @since 1.0.5
     */
    public <T> boolean hasMember(String key, T t) {
        return template.opsForSet().isMember(key, t);
    }

    /**
     * 批量移除 {@code key} 对应集合集合中指定的元素
     *
     * @param key 键
     * @param ts  指定的元素
     * @param <T> 泛型
     * @return 操作后的数组长度
     * @since 1.0.5
     */
    public <T> Long remove(String key, T... ts) {
        return template.opsForSet().remove(key, (Object) ts);
    }

    /**
     * 将 {@code key1} 对应集合中的元素 {@code t} 移动至 {@code key2} 对应的集合中
     *
     * <ol style="color:red">
     *     <li>当 {@code key2} 不存在时直接新增</li>
     *     <li>当 {@code t} 不存在时, 移动失败并返回 false</li>
     * </ol>
     *
     * @param key1 键
     * @param t    key1 中的元素
     * @param key2 键
     * @param <T>  泛型
     * @return true/false
     * @since 1.0.5
     */
    public <T> boolean move(String key1, T t, String key2) {
        return template.opsForSet().move(key1, t, key2);
    }

    /**
     * 随机弹出 key 对应集合中的元素
     *
     * @param key 键
     * @param <T> 泛型
     * @return 返回随即弹出的元素
     * @since 1.0.5
     */
    public <T> T pop(String key) {
        return (T) template.opsForSet().pop(key);
    }

    /**
     * 随即弹出 key 对应集合中指定个数（{@code num}）的元素
     *
     * @param key 键
     * @param num 指定弹出的元素个数
     * @param <T> 泛型
     * @return 弹出的元素集合
     * @since 1.0.5
     */
    public <T> List<T> pop(String key, long num) {
        return (List<T>) template.opsForSet().pop(key, num);
    }

    /**
     * 获取 {@code key1} 与另一个 {@code key2} 集合之间的差值
     *
     * @param key1 键
     * @param key2 键
     * @param <T>  泛型
     * @return 两个集合之间的差值元素集合
     * @since 1.0.5
     */
    public <T> Set<T> difference(String key1, String key2) {
        return (Set<T>) template.opsForSet().difference(key1, key2);
    }

    /**
     * 获取 {@code key1} 对应集合与 {@code key2} 对应集合的交集元素
     *
     * @param key1 键
     * @param key2 键
     * @param <T>  泛型
     * @return 两个集合的交集元素集合
     * @since 1.0.5
     */
    public <T> Set<T> intersect(String key1, String key2) {
        return (Set<T>) template.opsForSet().intersect(key1, key2);
    }

    /**
     * 获取多个 key 对应集合的交集元素
     *
     * @param key        键
     * @param collection 多个键的集合
     * @param <T>        泛型
     * @return 多个集合的交集元素集合
     * @since 1.0.5
     */
    public <T> Set<T> intersect(String key, Collection<String> collection) {
        return (Set<T>) template.opsForSet().intersect(key, collection);
    }

    /**
     * 获取两个 {@code key1}、{@code key2} 集合的合集, 并且去重
     *
     * @param key1 键
     * @param key2 键
     * @param <T>  泛型
     * @return 已去重后的合集
     * @since 1.0.5
     */
    public <T> Set<T> union(String key1, String key2) {
        return (Set<T>) template.opsForSet().union(key1, key2);
    }

    /**
     * 添加元素
     *
     * <p style="color: yellow">有序集合按照元素的 {@code score} 值由小到大进行排列</p>
     *
     * @param key   键
     * @param value 元素
     * @param score 评分
     * @param <T>   泛型
     * @since 1.0.6
     */
    public <T> void addZSet(String key, T value, double score) {
        template.opsForZSet().add(key, value, score);
    }

    /**
     * 删除指定 {@code key} 对应的元素值
     *
     * @param key 键
     * @param ts  元素
     * @param <T> 泛型
     * @return 操作后的数组长度
     * @since 1.0.6
     */
    public <T> Long drop(String key, T... ts) {
        return template.opsForZSet().remove(key, (Object) ts);
    }

    /**
     * 增加指定 {@code key} 元素对应 {@code value} 的 {@code score} 评分值
     *
     * @param key   键
     * @param value 指定的元素
     * @param score 新的评分值
     * @param <T> 泛型
     * @return 返回增加后的值
     * @since 1.0.6
     */
    public <T> Double addScore(String key, T value, double score) {
         return template.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * 返回指定 {@code key} 集合中元素为 {@code value} 索引
     *
     * @param key   键
     * @param value 指定的元素
     * @param <T> 泛型
     * @return 指定元素的索引
     * @since 1.0.6
     */
    public <T> Long rank(String key, T value) {
         return template.opsForZSet().rank(key, value);
    }

    /**
     * 当评分从高到低时, 返回排序集中具有值的元素的索引
     *
     * @param key   键
     * @param value 指定的元素
     * @param <T>   泛型
     * @return 指定元素的索引
     * @since 1.0.6
     */
    public <T> Long reverseRank(String key, T value) {
         return template.opsForZSet().reverseRank(key, value);
    }

    /**
     * 获取指定集合中给定区间（下标）的元素集合
     *
     * <p style="color: yellow">{@code score} 值由小到大显示</p>
     *
     * <p style="color: red">[0, -1] 表示查询所有</p>
     *
     * @param key   键
     * @param start 起始下标
     * @param end   结束下标
     * @param <T>   泛型
     * @return 给定区间的元素集合
     * @since 1.0.6
     */
    public <T> Set<T> range(String key, long start, long end) {
        return (Set<T>) template.opsForZSet().range(key, start, end);
    }

    /**
     * 获取指定集合中给定区间（下标）的元素集合
     *
     * <p style="color: yellow">{@code score} 值由大到小显示</p>
     *
     * <p style="color: red">[0, -1] 表示查询所有</p>
     *
     * @param key   键
     * @param start 起始下标
     * @param end   结束下标
     * @param <T>   泛型
     * @return 给定区间的元素集合
     * @since 1.0.6
     */
    public <T> Set<T> reverseRange(String key, long start, long end) {
        return (Set<T>) template.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取按照给定 {@code score} 值范围的集合
     *
     * <p style="color: yellow">查询 {@code min} 至 {@code max} 之间的 {@code score} 元素, {@code score} 结果从小到大排序</p>
     *
     * @see Pair
     * @param key 键
     * @param min 最小 {@code score} 值
     * @param max 最大 {@code score} 值
     * @param <T> 泛型
     * @return 给定区间的集合
     * @since 1.0.6
     */
    public <T> List<Pair<T, Double>> rangeByScore(String key, double min, double max) {
        return template
                .opsForZSet()
                .rangeByScoreWithScores(key, min, max)
                .stream()
                .map(v -> Pair.of((T) v.getValue(), v.getScore()))
                .collect(Collectors.toList());
    }

    /**
     * 获取按照给定 {@code score} 值范围的集合
     *
     * <p style="color: yellow">查询 {@code min} 至 {@code max} 之间的 {@code score} 元素, {@code score} 结果从大到小排序</p>
     *
     * @see Pair
     * @param key 键
     * @param min 最小 {@code score} 值
     * @param max 最大 {@code score} 值
     * @param <T> 泛型
     * @return 给定区间的集合
     * @since 1.0.6
     */
    public <T> List<Pair<T, Double>> reverseByScore(String key, double min, double max) {
        return template
                .opsForZSet()
                .reverseRangeByScoreWithScores(key, min, max)
                .stream()
                .map(v -> Pair.of((T) v.getValue(), v.getScore()))
                .collect(Collectors.toList());
    }

    /**
     * 获取指定集合按 {@code score} 从高到底指定 {@code score} 与下标范围内的元素
     *
     * <ol style="color: yellow">
     *     <li>{@code score} 范围: {@code min} 至 {@code max}</li>
     *     <li>下标范围: {@code start} 至 {@code end}</li>
     *     <li>{@code score} 从高到低排序</li>
     * </ol>
     *
     * <p style="color: red">[0, -1] 表示查询所有</p>
     *
     * @param key   键
     * @param min   最小 {@code score} 值
     * @param max   最大 {@code score} 值
     * @param start 起始下标
     * @param end   结束下标
     * @param <T>   泛型
     * @return 给定区间的集合
     * @since 1.0.6
     */
    public <T> Set<T> reverseRangeByScore(String key, double min, double max, long start, long end) {
        return (Set<T>) template.opsForZSet().reverseRangeByScore(key, min, max, start, end);
    }

    /**
     * 获取指定集合的 {@code score} 范围内的元素数量
     *
     * @param key 键
     * @param min 最小 {@code score} 值
     * @param max 最大 {@code score} 值
     * @return 元素数量
     * @since 1.0.6
     */
    public Long count(String key, double min, double max) {
        return template.opsForZSet().count(key, min, max);
    }

    /**
     * 获取指定集合的大小（元素个数）
     *
     * @param key 键
     * @return 元素个数
     * @since 1.0.6
     */
    public Long size(String key) {
        return template.opsForZSet().size(key);
    }

    /**
     * 获取集合中 {@code key}、{@code value} 元素对应的 {@code score} 值
     *
     * @param key   键
     * @param value 元素
     * @param <T>   泛型
     * @return {@code score} 值
     * @since 1.0.6
     */
    public <T> Double score(String key, T value) {
        return template.opsForZSet().score(key, value);
    }

    /**
     * 删除指定索引区间范围内的所有元素
     *
     * <p style="color: red">[0, -1] 表示查询所有</p>
     *
     * @param key   键
     * @param start 起始下标
     * @param end   结束下标
     * @return 删除的元素个数
     * @since 1.0.6
     */
    public Long removeRange(String key, long start, long end) {
        return template.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 移除指定 {@code score} 范围的集合成员
     *
     * @param key 键
     * @param min 最小 {@code score} 值
     * @param max 最大 {@code score} 值
     * @return 删除的元素个数
     * @since 1.0.6
     */
    public Long removeByScore(String key, double min, double max) {
        return template.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 获取 {@code key} 和 {@code key1} 的并集并存储在 {@code key2} 中
     *
     * <p style="color: red">{@code key1} 可以为单个字符串或者字符串集合</p>
     *
     * @param key  键
     * @param key1 键
     * @param key2 键
     * @return 目标集合的元素数
     * @since 1.0.6
     */
    public Long unionStore(String key, String key1, String key2) {
        return template.opsForZSet().unionAndStore(key, key1, key2);
    }

    /**
     * 获取 {@code key} 和 {@code key1} 的交集并存储在 {@code key2} 中
     *
     * <p style="color: red">{@code key1} 可以为单个字符串或者字符串集合</p>
     *
     * @param key  键
     * @param key1 键
     * @param key2 键
     * @return 目标集合的元素数
     * @since 1.0.6
     */
    public Long intersectStore(String key, String key1, String key2) {
        return template.opsForZSet().intersectAndStore(key, key1, key2);
    }

    /**
     * 获取指定集合的 {@code value}、{@code score} 值
     *
     * @see Pair
     * @param key 键
     * @return 指定集合
     * @param <T> 泛型
     * @since 1.0.6
     */
    public <T> List<Pair<T, Double>> scan(String key) {
        return template
                .opsForZSet()
                .scan(key, ScanOptions.NONE)
                .stream()
                .map(scan -> Pair.of((T) scan.getValue(), scan.getScore()))
                .collect(Collectors.toList());
    }

    /**
     * 单个添加 {@code hashKey} - {@code value} 键值对
     *
     * @param key     Redis 键
     * @param hashKey Hash 键
     * @param value   Hash 值
     * @param <K>     泛型
     * @param <V>     泛型
     * @since 1.0.6
     */
    public <K, V> void put(String key, K hashKey, V value) {
        template.opsForHash().put(key, hashKey, value);
    }

    /**
     * 批量添加 {@code hashKey} - {@code value} 键值对
     *
     * @param key 键
     * @param map {@link Map}
     * @param <K> 泛型
     * @param <V> 泛型
     * @since 1.0.6
     */
    public <K, V> void putAll(String key, Map<? extends K, ? extends V> map) {
        template.opsForHash().putAll(key, map);
    }

    /**
     * 仅当 {@code hashKey} 不存在时才添加
     *
     * @param key     Redis 键
     * @param hashKey Hash 键
     * @param value   Hash 值
     * @param <K>     泛型
     * @param <V>     泛型
     * @return true/false
     * @since 1.0.6
     */
    public <K, V> Boolean putIfAbsent(String key, K hashKey, V value) {
        return template.opsForHash().putIfAbsent(key, hashKey, value);
    }

    /**
     * 获取 {@code key} 对应的键值对
     *
     * @param key 键
     * @param <K> 泛型
     * @param <V> 泛型
     * @return {@link Map}
     * @since 1.0.6
     */
    public <K, V> Map<K, V> entries(String key) {
        return (Map<K, V>) template.opsForHash().entries(key);
    }

    /**
     * 获取指定 {@link Map} 键是否有值,如果存在则获取值，没有则返回 {@code null}
     *
     * @param key     Redis 键
     * @param hashKey Hash 键
     * @param <K>     泛型
     * @param <V>     泛型
     * @return 获取的值或 {@code null}
     * @since 1.0.6
     */
    public <K, V> V get(String key, K hashKey) {
        return (V) template.opsForHash().get(key, hashKey);
    }

    /**
     * 将哈希 {@code hashKey} 的值增加给定的 {@code delta} 增量
     *
     * @param key     Redis 键
     * @param hashKey Hash 键
     * @param delta   增量
     * @param <K>     泛型
     * @return 增加后的值
     * @since 1.0.6
     */
    public <K> Long incr(String key, K hashKey, long delta) {
        return template.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * 将哈希 {@code hashKey} 的值增加给定的 {@code delta} 增量
     *
     * @param key     Redis 键
     * @param hashKey Hash 键
     * @param delta   增量
     * @param <K>     泛型
     * @return 增加后的值
     * @since 1.0.6
     */
    public <K> Double incr(String key, K hashKey, double delta) {
        return template.opsForHash().increment(key, hashKey, delta);
    }

    /**
     * 删除指定 {@code key} 中一个或者多个 {@code hashKeys}
     *
     * @param key      Redis 键
     * @param hashKeys Hash 键
     * @param <K>      泛型
     * @return 操作成功的个数
     * @since 1.0.6
     */
    public <K> Long delete(String key, K... hashKeys) {
        return template.opsForHash().delete(key, hashKeys);
    }

    /**
     * 判断指定 {@code key} 中是否有指定的 {@code hashKeys} 键
     *
     * @param key      Redis 键
     * @param hashKeys Hash 键
     * @param <K>      泛型
     * @return true/false
     * @since 1.0.6
     */
    public <K> Boolean hasKey(String key, K hashKeys) {
        return template.opsForHash().hasKey(key, hashKeys);
    }

    /**
     * 获指定 {@code key} 中所有键
     *
     * @param key 键
     * @param <T> 泛型
     * @return 所有 Hash 键的集合
     * @since 1.0.6
     */
    public <T> Set<T> getKeys(String key) {
        return (Set<T>) template.opsForHash().keys(key);
    }

    /**
     * 获指定 {@code key} 中所有值
     *
     * @param key 键
     * @param <T> 泛型
     * @return 所有 Hash 键对应的 Value 值
     * @since 1.0.6
     */
    public <T> List<T> values(String key) {
        return (List<T>) template.opsForHash().values(key);
    }

    /**
     * 获取指定 Hash 表中键的数量
     *
     * @param key 键
     * @return Hash 表中键的数量
     * @since 1.0.6
     */
    public Long numbers(String key) {
        return template.opsForHash().size(key);
    }

}