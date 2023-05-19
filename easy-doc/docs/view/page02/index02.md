### ORM 配置

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

#### RootMapper 介绍

---

::: tip 说明
默认装配了 MyBatis-Plus 的分页拦截器（MySQL）, 可以使用 SpringBoot 的启动注解进行排除（当然你不想使用的话）  
扩充了 Mapper 接口方法, 继承 RootMapper 后, service 实现类可以不用继承 MyBatis-Plus 的 ServiceImpl 类即可获得 CRUD 功能
:::

~~~java
public interface UserMapper extends RootMapper<User> {
    
}
~~~


#### 自动填充配置

---

::: tip 说明
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

#### 业务基础字段

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


#### 数据权限

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

#### 本地线程副本工具

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