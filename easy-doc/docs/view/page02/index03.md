### 缓存工具

Maven:
~~~xml
<dependency>
    <groupId>com.github.Ling2099</groupId>
    <artifactId>easy-cache</artifactId>
    <version>latest version</version>
</dependency>
~~~

Gradle:
~~~gradle
implementation 'com.github.Ling2099:easy-cache:latest version'
// 或者
implementation group: 'com.github.Ling2099', name: 'easy-cache', version: 'latest version'
~~~

::: tip 说明
默认对 RedisTemplate 进行了配置, 且开启了缓存注解, 当然你也可以使用 Spring 工具从容器中移除（如果你不想使用的话）  
相应的, 你可以使用诸如 <b>@Cacheable</b>、<b>@CachePut</b>、<b>@CacheEvict</b> 等注解进行缓存数据的相关操作。  
另外整合了 Redis 相关的工具方法 RedisTool, 如下
:::

#### &#x1F6A2; key 相关操作

| 方法名 | 入参参数 | 说明 |
| ---- | ---- | ---- |
| hasKey | String | 校验 key 是否存在 |
| keys | String | 获取缓存的基本对象列表 |

#### &#x1F697; 过期时间相关

| 方法名 | 入参参数 | 说明 |
| ---- | ---- | ---- |
| getExpire | String | 获取指定 key 的剩余时间 |
| expire | String, long | 对指定的数据设置过期时间, 以秒为单位 |
| expire | String, long, TimeUnit | 对指定的数据设置过期时间 |

#### &#x1F699; 递增递减

| 方法名 | 入参参数 | 说明 |
| ---- | ---- | ---- |
| incr | String | 原子性递增 |
| decr | String | 原子性递减 |

#### &#x1F69A; 字符串类型（String）

| 方法名 | 入参参数 | 说明 |
| ---- | ---- | ---- |
| get | String | 通过指定 key 获取 Redis 缓存数据 |
| set | String, Object | 缓存基本数据 |
| set | String, Object, Long | 缓存基本数据, 以秒为单位 |
| set | String, Object, Long, TimeUnit | 缓存基本数据 |

#### &#x1F6A4; 列表类型（List）

| 方法名 | 入参参数 | 说明 |
| ---- | ---- | ---- |
| setLeft | String, Object | 将数据插入列表的头部 |
| setLeft | String, Object[] | 将数据插入列表的头部 |
| setLeft | String, List | 将集合全部插入列表的头部 |
| getLeft | String | 删除并返回列表中的第一个头部的元素 |
| getLeft | String, long | 删除并返回列表中从头部开始的多个元素 |
| getLeft | String, long, TimeUnit | 删除并返回列表中的第一个头部的元素 |
| setRight | String, Object | 将数据插入列表的尾部 |
| setRight | String, Object[] | 将数据插入列表的尾部 |
| setRight | String, List | 将集合全部插入列表的尾部 |
| getRight | String | 删除并返回列表中的第一个尾部的元素 |
| getRight | String, long | 删除并返回列表中从尾部部开始的多个元素 |
| getRight | String, long, TimeUnit | 删除并返回列表中的第一个尾部的元素 |



