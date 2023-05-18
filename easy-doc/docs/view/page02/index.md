# 安装

集成 easy-all 版本要求如下:

+ JDK 8+
+ Maven or Gradle

## 基础依赖

Maven:
~~~xml
<dependency>
    <groupId>com.github.Ling2099</groupId>
    <artifactId>easy-basic</artifactId>
    <version>latest version</version>
</dependency>
~~~

Gradle:
~~~gradle
implementation 'com.github.Ling2099:easy-basic:latest version'
// 或者
implementation group: 'com.github.Ling2099', name: 'easy-basic', version: 'latest version'
~~~

### 响应工具
---
::: tip tips
封装了基础的 web 接口响应方法, 且实现单例, 避免频繁被创建
:::

~~~java
@GetMapping
public ResultVo<List<Integer>> getList() {
    return ResultVo.ok(Arrays.asList(1, 2, 3));
}
~~~

+ 响应属性及方法

| 名称 | 类型 | 说明 |
| ---- | ---- | ---- |
| code | int | 响应状态码 |
| msg | String | 响应信息 |
| data | `<T>` | 响应数据 |

<table>
    <tr>
        <th>方法名</th>
        <th>入参参数</th>
        <th>说明</th>
    </tr>
    <tr>
        <th rowspan="4">ok()</th>
        <td>无</td>
        <td rowspan="4">响应成功消息</td>
    </tr>
    <tr>
        <td>msg</td>
    </tr>
    <tr>
        <td>data</td>
    </tr>
    <tr>
        <td>msg、data</td>
    </tr>
    <tr>
        <th rowspan="7">fail()</th>
        <td>无</td>
        <td rowspan="7">响应失败消息</td>
    </tr>
    <tr>
        <td>msg</td>
    </tr>
    <tr>
        <td>data</td>
    </tr>
    <tr>
        <td>msg、data</td>
    </tr>
    <tr>
        <td>StatusEnum</td>
    </tr>
    <tr>
        <td>StatusEnum、msg</td>
    </tr>
    <tr>
        <td>StatusEnum、msg、data</td>
    </tr>
</table>

+ 响应状态码和对应的响应信息（StatusEnum）

| 状态码编号 | 响应信息 |
| ---- | ---- |
| 200 | 操作成功 |
| 401 | 未授权 |
| 403 | 访问受限，授权过期 |
| 404 | 资源未找到 |
| 423 | 您的账号已从其它设备登录, 请注意隐私保护 |
| 429 | 操作过于频繁, 请稍后再试 |
| 500 | 系统内部错误 |

### 基础异常
---
::: tip tips
简单的基础异常类, 可以配合 easy-web 中的全局异常捕获
:::

~~~java
public void div() {
    try {
        int num = 1 / 0;
    } catch (Exception e) {
        throw new BaseException("捕获异常");
    }
}
~~~

### 线程工具
---
::: tip tips
封装了异步任务、定时任务、延迟任务的线程池工具方法
:::

~~~java
public void execute() {
    TaskPool.execute(() -> System.out.println("这是一个异步线程"));
}
~~~

<table>
    <tr>
        <th>分类</th>
        <th>方法名</th>
        <th>入参参数</th>
        <th>返回值</th>
        <th>说明</th>
    </tr>
    <tr>
        <td rowspan="2">异步任务</td>
        <td>execute</td>
        <td>Runnable</td>
        <td>void</td>
        <td>无返回值的异步线程</td>
    </tr>
    <tr>
        <td>submit</td>
        <td>Callable&lt;V&gt;</td>
        <td>Future&lt;V&gt;</td>
        <td>有返回值的异步线程</td>
    </tr>
    <tr>
        <td rowspan="2">延迟队列</td>
        <td>push</td>
        <td>long、T、TimeUnit</td>
        <td>void</td>
        <td>向延迟队列中添加元素</td>
    </tr>
    <tr>
        <td>pull</td>
        <td>无</td>
        <td>T</td>
        <td>取出延迟队列中的元素</td>
    </tr>
    <tr>
        <td rowspan="3">定时任务</td>
        <td>plan</td>
        <td>Runnable、String、String、long、TimeUnit</td>
        <td>void</td>
        <td>指定首次延时执行时间点, 后续根据频率执行</td>
    </tr>
    <tr>
        <td>plan</td>
        <td>Runnable、String、long、long、TimeUnit</td>
        <td>void</td>
        <td>指定首次延时执行时长, 后续根据频率执行</td>
    </tr>
    <tr>
        <td>cancel</td>
        <td>String</td>
        <td>void</td>
        <td>取消定时任务</td>
    </tr>
    <tr>
        <td rowspan="2">关闭线程池</td>
        <td>closeScheduled</td>
        <td>无</td>
        <td>void</td>
        <td>关闭定时任务线程池</td>
    </tr>
    <tr>
        <td>closeThreadPool</td>
        <td>无</td>
        <td>void</td>
        <td>关闭异步任务线程池</td>
    </tr>
</table>

## ORM 配置

Maven:
~~~xml
<dependency>
    <groupId>com.github.Ling2099</groupId>
    <artifactId>easy-orm</artifactId>
    <version>latest version</version>
</dependency>
~~~

Gradle:
~~~gradle
implementation 'com.github.Ling2099:easy-orm:latest version'
// 或者
implementation group: 'com.github.Ling2099', name: 'easy-orm', version: 'latest version'
~~~

### RootMapper 介绍

---

::: tip tips
默认装配了 MyBatis-Plus 的分页拦截器（MySQL）, 可以使用 SpringBoot 的启动注解进行排除（当然你不想使用的话）  
扩充了 Mapper 接口方法, 继承 RootMapper 后, service 实现类可以不用继承 MyBatis-Plus 的 ServiceImpl 类即可获得 CRUD 功能
:::

~~~java
public interface UserMapper extends RootMapper<User> {
    
}
~~~


### 自动填充配置

---

::: tip tips
默认管理了基础字段的自动填充（未注入 Spring 容器）功能, 如需扩展可以参照如下代码
:::

~~~java
@Configuration
public class YourConfig extends MetaObjectConfig {

    @Override
    public void insertFill(MetaObject meta) {
        super.insertFill(meta);
        // 你的填充逻辑   
    }

    @Override
    public void updateFill(MetaObject meta) {
        super.updateFill(meta);
        // 你的填充逻辑
    }
}
~~~

### 业务基础字段

---

| 字段名 | 映射名 | 说明 |
| ---- | ---- | ---- |
| creator_id | creatorId | 创建人 ID |
| creator | creator | 创建人 |
| create_time | createTime | 创建时间 |
| modifier_id | modifierId | 修改人 ID |
| modifier | modifier | 修改人 |
| modify_time | modifyTime | 修改时间 |
| org_id | orgId | 当前机构节点 ID |
| scope | scope | 数据权限范围 |
| has_del | hasDel | 逻辑删除（0: 未删除/1: 已删除）


### 数据权限

---

::: tip 说明
如需使用, 可以在项目配置文件中开启业务数据权限代理; 此代理会根据本地线程副本中的权限字符进行判别构造出 SQL 片段, 具体使用如下
:::

~~~sql
DROP TABLE IF EXISTS `你的表名`;
CREATE TABLE `你的表名` (
  其它字段...
  `scope` varchar(32) CHARACTER DEFAULT NULL COMMENT '数据权限字符'
  数据库中存储类似 001003008006 这样的值
) 
~~~

~~~yml
# 开启数据权限代理
easy:
  scope: true
~~~

~~~java
/**
 * 分页对象继承 PageEntity
 * 
 * PageEntity 默认自带 pageNum 和 pageSize 属性, 且继承自 PageScope
 */
public class YourPage extends PageEntity {

    // 你的分页域

}

/**
 * 或者你的其它对象可以直接继承 PageScope
 */
class YourOtherObject extends PageScope {

    // 域

}
~~~

当然你得在每次请求时, 向本地线程副本中设置数据权限类型（详细见后面的本地线程副本工具）
~~~java
@Service
public class YourServiceImpl {

    /**
     * 需要被数据权限代理的方法, 必须以 find 或 query 开头
     *
     * 具体的数据权限类型和对应的 SQL 片段如下
     * 1. 全部数据权限（无 SQL 条件）
     * 2. 本级及以下数据权限 （scope LIKE '?%'）
     * 3. 本级数据权限 （scope = ?）
     * 4. 仅本人数据权限 （userId = ?）
     * 5. 自定义数据权限 （orgId IN (?, ?, ?)）
     */
    public void findPage(YourPage page) {
        // 获取 SQL 片段以做实际的拼接
        String chip = page.getChip();
    }

}
~~~

### 本地线程副本工具

---

::: tip 说明
ThreadLocalTool 默认提供了如下方法和 header 属性
:::

~~~java
// 获取 ThreadLocal 值
T get(String key);
// 设置 ThreadLocal 值
void set(String key, T value);
// 移除当前线程的副本数据
void remove()
~~~

具体可查看 FieldsConstant 字符常量池
| 属性名 | 属性值 | 说明 |
| ---- | ---- | ---- |
| USER_ID | userId | 用户 ID |
| USER_NAME | userName | 用户名 |
| ORG_ID | orgId | 机构 ID |
| ORG_IDS | orgIds | 机构 ID 集合 |
| SCOPE | scope | 数据权限范围 |
| SCOPE_TYPE | scopeType | 数据权限标识 |
| HAS_DEL | hasDel | 是否删除 |
| CREATOR_ID | creatorId | 创建人 ID |
| CREATOR | creator | 创建人 |
| CREATE_TIME | createTime | 创建时间 |
| MODIFIER_ID | modifierId | 修改人 ID |
| MODIFIER | modifier | 修改人 |
| MODIFY_TIME | modifyTime | 修改时间 |

::: tip 说明
为了方便获取当先线程的存储值, 可以使用 StatementTool 工具类, 如下
:::

~~~java
// 获取当前用户主键 ID
Integer getUserId();
// 获取当前用户名
String getUserName();
// 获取当前用户的机构 ID
Integer getOrgId();
// 获取当前用户所拥有的机构 ID 集合字符
String getOrgIds();
// 获取当前用户的数据权限
String getScope();
// 获取当前用户的数据权限类型
Integer getScopeType();
~~~

## 缓存工具

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

## 文件相关

Maven:
~~~xml
<dependency>
    <groupId>com.github.Ling2099</groupId>
    <artifactId>easy-file</artifactId>
    <version>latest version</version>
</dependency>
~~~

Gradle:
~~~gradle
implementation 'com.github.Ling2099:easy-file:latest version'
// 或者
implementation group: 'com.github.Ling2099', name: 'easy-file', version: 'latest version'
~~~

## web 整合

Maven:
~~~xml
<dependency>
    <groupId>com.github.Ling2099</groupId>
    <artifactId>easy-web</artifactId>
    <version>latest version</version>
</dependency>
~~~

Gradle:
~~~gradle
implementation 'com.github.Ling2099:easy-web:latest version'
// 或者
implementation group: 'com.github.Ling2099', name: 'easy-web', version: 'latest version'
~~~