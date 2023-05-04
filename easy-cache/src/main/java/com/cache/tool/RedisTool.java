package com.cache.tool;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.List;
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
    public <T> long setLeft(final String key, final T value) {
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
    public <T> long setLeft(final String key, final T... values) {
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
    public <T> long setLeft(final String key, final List<T> list) {
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
    public <T> long setRight(final String key, final T value) {
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
    public <T> long setRight(final String key, final T... value) {
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
    public <T> long setRight(final String key, final List<T> list) {
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

}
