package com.cache.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.*;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 配置 Redis 的 {@link Cacheable} 注解
 *
 * <ul>
 *     <li>
 *         {@link Cacheable}
 *         <ol>
 *             <li><b>{@link Cacheable#cacheNames()}</b><br/> 指定缓存组件的名字, 将方法的返回结果放在哪个缓存中, 可以是数组的方式指定多个缓存</li>
 *             <li><b>{@link Cacheable#value()}</b><br/> 指定缓存组件的名字, 将方法的返回结果放在哪个缓存中, 可以是数组的方式指定多个缓存</li>
 *             <li>
 *                 <b>{@link Cacheable#key()}</b><br/> 缓存数据时使用的 key, 可以用它来指定. 默认是使用方法参数的值, 也可以使用 SpEL 表达式
 *                 <table style="border: 1px dotted red">
 *                     <tr style="border-bottom: 1px dotted red;">
 *                         <td style="border-right: 1px dotted red;">名字</td>
 *                         <td style="border-right: 1px dotted red;">位置</td>
 *                         <td style="border-right: 1px dotted red;">描述</td>
 *                         <td>示例</td>
 *                     </tr>
 *                     <tr style="border-bottom: 1px dotted red;">
 *                         <td style="border-right: 1px dotted red;">methodName</td>
 *                         <td style="border-right: 1px dotted red;">root object</td>
 *                         <td style="border-right: 1px dotted red;">当前被调用的方法名</td>
 *                         <td>#root.methodName</td>
 *                     </tr>
 *                     <tr style="border-bottom: 1px dotted red;">
 *                         <td style="border-right: 1px dotted red;">method</td>
 *                         <td style="border-right: 1px dotted red;">root object</td>
 *                         <td style="border-right: 1px dotted red;">当前被调用的方法</td>
 *                         <td>#root.method.name</td>
 *                     </tr>
 *                     <tr style="border-bottom: 1px dotted red;">
 *                         <td style="border-right: 1px dotted red;">target</td>
 *                         <td style="border-right: 1px dotted red;">root object</td>
 *                         <td style="border-right: 1px dotted red;">当前被调用的目标对象</td>
 *                         <td>#root.target</td>
 *                     </tr>
 *                     <tr style="border-bottom: 1px dotted red;">
 *                         <td style="border-right: 1px dotted red;">targetClass</td>
 *                         <td style="border-right: 1px dotted red;">root object</td>
 *                         <td style="border-right: 1px dotted red;">当前被调用的目标对象类</td>
 *                         <td>#root.targetClass</td>
 *                     </tr>
 *                     <tr style="border-bottom: 1px dotted red;">
 *                         <td style="border-right: 1px dotted red;">args</td>
 *                         <td style="border-right: 1px dotted red;">root object</td>
 *                         <td style="border-right: 1px dotted red;">当前被调用的方法的参数列表</td>
 *                         <td>#root.args[0]</td>
 *                     </tr>
 *                     <tr style="border-bottom: 1px dotted red;">
 *                         <td style="border-right: 1px dotted red;">caches</td>
 *                         <td style="border-right: 1px dotted red;">root object</td>
 *                         <td style="border-right: 1px dotted red;">当前方法调用使用的缓存列表(如 @Cacheable(value={"cache1", "cache2"}), 则有两个 cache)</td>
 *                         <td>#root.caches[0].name</td>
 *                     </tr>
 *                     <tr style="border-bottom: 1px dotted red;">
 *                         <td style="border-right: 1px dotted red;">argument name</td>
 *                         <td style="border-right: 1px dotted red;">evaluation context</td>
 *                         <td style="border-right: 1px dotted red;">方法参数的名字, 可以直接使用参数名, 也可以使用 #p0 或 #a0 的形式, 0 代表参数的索引</td>
 *                         <td>#a0</td>
 *                     </tr>
 *                     <tr>
 *                         <td style="border-right: 1px dotted red;">result</td>
 *                         <td style="border-right: 1px dotted red;">evaluation context</td>
 *                         <td style="border-right: 1px dotted red;">方法执行后的返回值(仅当方法执行后的判断有效)</td>
 *                         <td>#result</td>
 *                     </tr>
 *                 </table>
 *             </li>
 *         </ol>
 *     </li>
 *     <li>
 *         {@link CacheEvict}<br/>
 *         用来标注在需要清除缓存元素的方法或类上
 *         <b>
 *             allEntries 是 boolean 类型, 表示是否需要清除缓存中的所有元素. 默认为false, 表示不需要. 当指定了 allEntries 为 true 时, Spring Cache 将忽略指定的key.
 *             有的时候我们需要 cache 一下清除所有的元素, 这比一个一个清除元素更有效率
 *         </b>
 *     </li>
 *     <li>
 *         {@link CachePut}<br/>
 *         不管缓存有没有, 都将方法的返回结果写入缓存; 适用于缓存更新
 *     </li>
 *     <li>
 *         {@link Caching}<br/>
 *         该注解可以让我们在一个方法或者类上同时指定多个 Spring Cache 相关的注解. 其拥有三个属性: cacheable、 put 和 evict,
 *         分别用于指定 @Cacheable、 @CachePut 和 @CacheEvict.
 *     </li>
 * </ul>
 *
 * @author LZH
 * @version 1.0.5
 * @since 2023/06/01
 */
@EnableCaching
@ConditionalOnProperty(name = "easy.cache")
public class CachingConfig implements CachingConfigurer {

    /**
     * 日志记录: {@link Logger}
     */
    private static final Logger log = Logger.getLogger("com.cache.conf.CachingConfig");

    /**
     * 配置 Spring 缓存管理（诸如 {@link org.springframework.cache.annotation.Cacheable} 注解）
     *
     * @param factory {@link RedisConnectionFactory}
     * @return {@link CacheManager}
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 配置序列化（解决乱码的问题）, 过期时间 600 秒
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            // 设置缓存的默认过期时间 ==> 注意: 这里后面改为配置文件
            .entryTtl(Duration.ofSeconds(600))
            // cacheable key 双冒号变为单冒号
            .computePrefixWith(this::convert)
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class)))
            // 不缓存空值
            .disableCachingNullValues();

        return RedisCacheManager
            .builder(factory)
            .cacheDefaults(config)
            .build();
    }

    /**
     * cacheable key 双冒号变为单冒号
     *
     * @param name 双冒号字符
     * @return 单冒号字符
     */
    private String convert(String name) {
        return ":";
    }

    /**
     * Redis 数据操作异常处理. 该方法处理逻辑: 在日志中打印出错误信息, 但是放行
     *
     * <p>保证 Redis 服务器出现连接等问题的时候不影响程序的正常运行</p>
     *
     * @return {@link CacheErrorHandler}
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache,
                                            Object key, Object value) {
                log.log(Level.SEVERE, exception.getMessage(), exception);
            }

            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache,
                                            Object key) {
                log.log(Level.SEVERE, exception.getMessage(), exception);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache,
                                              Object key) {
                log.log(Level.SEVERE, exception.getMessage(), exception);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                log.log(Level.SEVERE, exception.getMessage(), exception);
            }
        };
    }

}
