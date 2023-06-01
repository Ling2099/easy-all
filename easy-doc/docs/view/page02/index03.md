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

#### 缓存注解
---

::: tip 说明
可在配置文件中开启 SpringBoot Redis 的缓存注解, 如 <b>@Cacheable</b>、<b>@CachePut</b>、<b>@CacheEvict</b> 等
:::

~~~yaml
easy:
  cache: true
~~~

#### Redis 客户端操作
---

::: tip 说明
默认对 RedisTemplate 进行了配置, 当然你也可以使用 Spring 工具从容器中移除（如果你不想使用的话）; 直接在你的代码中注入 RedisTool 即可
:::

~~~java
@Autowired
private RedisTool tool;
~~~

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

#### &#x1F681; 集合类型（Set）
| 方法名 | 入参参数 | 说明 |
| ---- | ---- | ---- |
| addSet | String, Object[] | 添加元素至集合中 |
| getMembers | String | 获取 key 中的所有值 |
| getSize | String | 获取 key 对应的集合长度 |
| random | String | 随即获取 key 对应的集合元素 |
| random | String, long | 随即获取 key 对应集合中指定个数的元素 |
| hasMember | String, Object | 判断 key 对应的集合中是否包含指定元素 |
| remove | String, Object[] | 批量移除 key 对应集合集合中指定的元素 |
| move | String, Object, String | 将 key1 对应集合中的元素移动至 key2 对应的集合中 |
| pop | String | 随机弹出 key 对应集合中的元素 |
| pop | String, long | 随即弹出 key 对应集合中指定个数的元素 |
| difference | String, String | 获取 key1 与另一个 key2 集合之间的差值 |
| intersect | String, String | 获取 key1 对应集合与 key2 对应集合的交集元素 |
| intersect | String, Collection | 获取多个 key 对应集合的交集元素 |
| union | String, String | 获取两个 key 集合的合集, 并且去重 |



