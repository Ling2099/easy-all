package com.cache.tool;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 自定义 Redis 工具类
 *
 * <ol>
 *     <li>
 *         key 相关操作
 *         <table style="border: 1px dotted red">
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">方法</td>
 *                 <td>描述</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #hasKey(String)}</td>
 *                 <td>校验 key 是否存在</td>
 *             </tr>
 *             <tr>
 *                 <td style="border-right: 1px dotted red;">{@link #keys(String)}</td>
 *                 <td>获取缓存的基本对象列表</td>
 *             </tr>
 *         </table>
 *     </li>
 *     <li>
 *         过期时间相关
 *         <table style="border: 1px dotted red">
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">方法</td>
 *                 <td>描述</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #getExpire(String)}</td>
 *                 <td>获取指定 key 的剩余时间</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #expire(String, long)}</td>
 *                 <td>对指定的数据设置过期时间, 以秒为单位</td>
 *             </tr>
 *             <tr>
 *                 <td style="border-right: 1px dotted red;">{@link #expire(String, long, TimeUnit)}</td>
 *                 <td>对指定的数据设置过期时间</td>
 *             </tr>
 *         </table>
 *     </li>
 *     <li>
 *         递增递减
 *         <table style="border: 1px dotted red">
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">方法</td>
 *                 <td>描述</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #incr(String)}</td>
 *                 <td>原子性递增</td>
 *             </tr>
 *             <tr>
 *                 <td style="border-right: 1px dotted red;">{@link #decr(String)}</td>
 *                 <td>原子性递减</td>
 *             </tr>
 *         </table>
 *     </li>
 *     <li>
 *         字符串(String)
 *         <table style="border: 1px dotted red">
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">方法</td>
 *                 <td>描述</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #get(String)}</td>
 *                 <td>通过指定 key 获取 Redis 缓存数据</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #set(String, Object)}</td>
 *                 <td>缓存基本数据</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #set(String, Object, Long)}</td>
 *                 <td>缓存基本数据, 以秒为单位</td>
 *             </tr>
 *             <tr>
 *                 <td style="border-right: 1px dotted red;">{@link #set(String, Object, Long, TimeUnit)}</td>
 *                 <td>缓存基本数据</td>
 *             </tr>
 *         </table>
 *     </li>
 *     <li>
 *         列表(List)
 *         <table style="border: 1px dotted red">
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">方法</td>
 *                 <td>描述</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #setLeft(String, Object)}</td>
 *                 <td>将数据插入列表的头部</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #setLeft(String, Object[])}</td>
 *                 <td>将数据插入列表的头部</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #setLeft(String, List)}</td>
 *                 <td>将集合全部插入列表的头部</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #getLeft(String)}</td>
 *                 <td>删除并返回列表中的第一个头部的元素</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #getLeft(String, long)}</td>
 *                 <td>删除并返回列表中从头部开始的多个元素</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #getLeft(String, long, TimeUnit)}</td>
 *                 <td>删除并返回列表中的第一个头部的元素</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #setRight(String, Object)} </td>
 *                 <td>将数据插入列表的尾部</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #setRight(String, Object[])} </td>
 *                 <td>将数据插入列表的尾部</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #setRight(String, List)} </td>
 *                 <td>将集合全部插入列表的尾部</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #getRight(String)} </td>
 *                 <td>删除并返回列表中的第一个尾部的元素</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #getRight(String, long)} </td>
 *                 <td>删除并返回列表中从尾部部开始的多个元素</td>
 *             </tr>
 *             <tr>
 *                 <td style="border-right: 1px dotted red;">{@link #getRight(String, long, TimeUnit)} </td>
 *                 <td>删除并返回列表中的第一个尾部的元素</td>
 *             </tr>
 *         </table>
 *     </li>
 *     <li>
 *         集合(Set)
 *         <table style="border: 1px dotted red">
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">方法</td>
 *                 <td>描述</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #addSet(String, Object[])}</td>
 *                 <td>添加元素至集合中</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #getMembers(String)}</td>
 *                 <td>获取 key 中的所有值</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #getSize(String)}</td>
 *                 <td>获取 key 对应的集合长度</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #random(String)}</td>
 *                 <td>随即获取 key 对应的集合元素</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #random(String, long)}</td>
 *                 <td>随即获取 key 对应集合中指定个数的元素</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #hasMember(String, Object)}</td>
 *                 <td>判断 key 对应的集合中是否包含指定元素</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #remove(String, Object[])}</td>
 *                 <td>批量移除 key 对应集合集合中指定的元素</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #move(String, Object, String)}</td>
 *                 <td>将 key1 对应集合中的元素移动至 key2 对应的集合中</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #pop(String)}</td>
 *                 <td>随机弹出 key 对应集合中的元素</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #pop(String, long)}</td>
 *                 <td>随即弹出 key 对应集合中指定个数的元素</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #difference(String, String)}</td>
 *                 <td>获取 key1 与另一个 key2 集合之间的差值</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #intersect(String, String)}</td>
 *                 <td>获取 key1 对应集合与 key2 对应集合的交集元素</td>
 *             </tr>
 *             <tr style="border-bottom: 1px dotted red;">
 *                 <td style="border-right: 1px dotted red;">{@link #intersect(String, Collection)}</td>
 *                 <td>获取多个 key 对应集合的交集元素</td>
 *             </tr>
 *             <tr>
 *                 <td style="border-right: 1px dotted red;">{@link #union(String, String)}</td>
 *                 <td>获取两个 key 集合的合集, 并且去重</td>
 *             </tr>
 *         </table>
 *     </li>
 *     <li>
 *         有序集合(Zset)
 *     </li>
 *     <li>
 *         哈希表(Hash)
 *     </li>
 * </ol>
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
@SuppressWarnings(value = {"unchecked", "ClassCanBeRecord", "ConstantConditions"})
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
    public boolean hasKey(final String key) {
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
    public Collection<String> keys(final String pattern) {
        return template.keys(pattern);
    }

    /**
     * 获取指定 key 的剩余时间
     *
     * @param key 键
     * @return 剩余时间, 单位秒
     */
    public Long getExpire(final String key) {
        return template.opsForValue().getOperations().getExpire(key);
    }

    /**
     * 对指定的数据设置过期时间, <b style="color:red">以秒为单位</b>
     *
     * @param key     键
     * @param timeout 过期时间
     * @return true or false
     */
    public Boolean expire(final String key, final long timeout) {
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
    public Boolean expire(final String key, final long timeout, final TimeUnit unit) {
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
    public Long incr(final String key) {
        return template.opsForValue().increment(key);
    }

    /**
     * 原子性递减
     *
     * @param key 键
     * @return 计算后的结果
     */
    public Long decr(final String key) {
        return template.opsForValue().decrement(key);
    }

    /**
     * 通过指定 key 获取 Redis 缓存数据
     *
     * @param key 键
     * @param <T> 泛型
     * @return T 缓存数据
     */
    public <T> T get(final String key) {
        return (T) template.opsForValue().get(key);
    }

    /**
     * 缓存基本数据
     *
     * @param key   键
     * @param value 数据
     * @param <T>   泛型
     */
    public <T> void set(final String key, final T value) {
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
    public <T> void set(final String key, final T value, final Long timeout) {
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
    public <T> void set(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
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
    public <T> Long setLeft(final String key, final T value) {
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
    public <T> Long setLeft(final String key, final T... values) {
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
    public <T> Long setLeft(final String key, final List<T> list) {
        return template.opsForList().leftPushAll(key, list);
    }

    /**
     * 删除并返回列表中的第一个<b style="color:red">头部</b>的元素
     *
     * @param key 键
     * @param <T> 泛型
     * @return 数据元素
     */
    public <T> T getLeft(final String key) {
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
    public <T> List<T> getLeft(final String key, final long count) {
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
    public <T> T getLeft(final String key, final long timeout, final TimeUnit unit) {
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
    public <T> Long setRight(final String key, final T value) {
        return template.opsForList().rightPush(key, value);
    }

    /**
     * 将数据插入列表的<b style="color:red">尾部</b>
     *
     * @param key   键
     * @param value 数据
     * @param <T>   泛型
     * @return 插入后的数组长度
     */
    public <T> Long setRight(final String key, final T... value) {
        return template.opsForList().rightPushAll(key, value);
    }

    /**
     * 将集合全部插入列表的<b style="color:red">尾部</b>
     *
     * @param key  键
     * @param list 数据集合
     * @param <T>  泛型
     * @return 插入后的数组长度
     */
    public <T> Long setRight(final String key, final List<T> list) {
        return template.opsForList().rightPushAll(key, list);
    }

    /**
     * 删除并返回列表中的第一个<b style="color:red">尾部</b>的元素
     *
     * @param key 键
     * @param <T> 泛型
     * @return 数据元素
     */
    public <T> T getRight(final String key) {
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
    public <T> List<T> getRight(final String key, final long count) {
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
    public <T> T getRight(final String key, final long timeout, final TimeUnit unit) {
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
    public <T> Long addSet(final String key, final T... ts) {
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
    public <T> Set<T> getMembers(final String key) {
        return (Set<T>) template.opsForSet().members(key);
    }

    /**
     * 获取 key 对应的集合长度
     *
     * @param key 键
     * @return 集合长度
     * @since 1.0.5
     */
    public Long getSize(final String key) {
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
    public <T> T random(final String key) {
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
    public <T> List<T> random(final String key, final long num) {
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
    public <T> boolean hasMember(final String key, final T t) {
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
    public <T> Long remove(final String key, final T... ts) {
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
    public <T> boolean move(final String key1, final T t, final String key2) {
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
    public <T> T pop(final String key) {
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
    public <T> List<T> pop(final String key, long num) {
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
    public <T> Set<T> difference(final String key1, final String key2) {
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
    public <T> Set<T> intersect(final String key1, final String key2) {
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
    public <T> Set<T> intersect(final String key, Collection<String> collection) {
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
    public <T> Set<T> union(final String key1, final String key2) {
        return (Set<T>) template.opsForSet().union(key1, key2);
    }

}
