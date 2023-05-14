---
title: 快速开始
---

## 构建项目

::: tip 提示
点击下载 [hy-cloud.exe](http://192.168.0.35:9000/hy-cloud/hy-cloud.exe) 可执行文件
:::

+ 双击执行, 依次键入**项目地址、 项目名称、 项目描述、 作者姓名**, 点击确定生成即可
+ 生成时会有简单校验; 写这个脚本的初心是方便大家快速构建项目, 不用到处复制
+ 最后导入 IDEA, 正常的 SpringBoot 启动方式启动就完事了
+ **注意: 启动之前联系管理员添加注册中心配置文件**

::: warning 注意
+ 若启动时报各种包找不到, IDEA 中打开终端（View --> Tool Windows --> Terminal）, 输入命令 mvn idea:idea 即可
+ 若遇到依赖不能从私服拉取下来的情况, 请复制以下配置至 Maven 的 settings.xml 中, 注意修改自己磁盘的仓库地址
:::

~~~xml
<?xml version="1.0" encoding="UTF-8"?>

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <!-- 本地仓库地址: 需要更改为自己的文件路径 -->
    <localRepository>D:\maven\apache-maven-3.8.1\repository</localRepository>

    <pluginGroups></pluginGroups>
    <proxies></proxies>

    <!-- servers -->
    <servers>
        <server>
            <id>releases</id>
            <username>admin</username>
            <password>admin123</password>
        </server>
        <server>
            <id>snapshots</id>
            <username>admin</username>
            <password>admin123</password>
        </server>
    </servers>

    <!-- mirrors -->
    <mirrors>
        <mirror>
            <id>releases</id>
            <mirrorOf>*</mirrorOf>
            <url>http://192.168.0.110:8081/nexus/content/groups/public/</url>
        </mirror>
        <mirror>
            <id>snapshots</id>
            <mirrorOf>*</mirrorOf>
            <url>http://192.168.0.110:8081/nexus/content/repositories/snapshots/</url>
        </mirror>
    </mirrors>

    <!-- 引入阿里云的镜像 -->
    <mirror>
        <id>alimaven</id>
        <mirrorOf>central</mirrorOf>
        <name>aliyun maven</name>
        <url>http://maven.aliyun.com/nexus/content/repositories/central/</url>
    </mirror>
    <mirror>
        <id>alimaven</id>
        <name>aliyun maven</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        <mirrorOf>central</mirrorOf>
    </mirror>

    <profiles>
        <profile>
            <id>jdk-1.8</id>
            <activation>
                <activeByDefault>true</activeByDefault>
                <jdk>1.8</jdk>
            </activation>
            <properties>
                <maven.compiler.source>1.8</maven.compiler.source>
                <maven.compiler.target>1.8</maven.compiler.target>
                <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
            </properties>
        </profile>

        <!--本地开发环境-->
        <profile>
            <id>dev</id>
            <properties>
                <profiles.active>dev</profiles.active>
            </properties>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>

        <!--测试环境-->
        <profile>
            <id>test</id>
            <properties>
                <profiles.active>test</profiles.active>
            </properties>
        </profile>

        <!--生产环境-->
        <profile>
            <id>prod</id>
            <properties>
                <profiles.active>prod</profiles.active>
            </properties>
        </profile>


        <profile>
            <id>nexus</id>
            <repositories>
                <repository>
                    <id>releases</id>
                    <url>http://releases</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </repository>
                <repository>
                    <id>snapshots</id>
                    <url>http://snapshots</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <id>releases</id>
                    <url>http://releases</url>
                    <releases>
                        <enabled>true</enabled>
                        <updatePolicy>always</updatePolicy>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </pluginRepository>
                <pluginRepository>
                    <id>snapshots</id>
                    <url>http://snapshots</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </pluginRepository>
            </pluginRepositories>
        </profile>
    </profiles>

    <activeProfiles>
        <activeProfile>nexus</activeProfile>
    </activeProfiles>
</settings>
~~~

## 项目结构

~~~
src
└─main
	├─java
	│  └─com
	│      └─hysoft
	│          └─test
	│              │  TestApplication.java    项目启动类
	│              │
	│              ├─constants                静态常量
	│              ├─controller               控制器
	│              ├─entity                   实体类
	│              │  ├─dto                   Dao 层与 Service 层或远程调用时数据传输对象
	│              │  ├─po                    分页对象
	│              │  ├─ro                    前端 Request 请求对象
	│              │  └─vo                    返回给前段的展示对象
	│              ├─generator
	│              │      CodeGenerator.java  代码生成器
	│              │
	│              ├─mapper                   Dao 层
	│              └─service                  Service 接口
	│                  └─impl                 Service 接口实现类
	└─resources
		│  bootstrap.yml                      项目配置文件（最高优先级）
		│
		└─mapper                              XML 文件夹
~~~

## 代码生成

::: warning 注意
数据库建立好表结构（详细内容请查看 [开发规范](#301)）, 利用 [MyBatis-Plus](https://baomidou.com/) 的代码生成器快捷的生成 MVC 各层基础代码
:::

::: tip 需要修改的有
+ 你的磁盘项目地址
+ 作者姓名
+ 数据库连接地址及密码账号
+ 包的配置信息
+ 其余的根据代码上的注释及自己需要, 增减配置项
+ 最后启动 main 方法输入表名即可
+ **值得注意的是, 除以上的个性化配置外, 其余配置项基本无需改动**
:::

## 项目演示

::: warning 开发基础流程
+ 数据库新建表 --> Mybatis-Plus 代码生成器生成 MVC 各层类、接口 --> 开始编写业务代码
+ 192.168.0.120 的数据库中新建了一个 hy-test 数据库, 其中构建了一张 test_user 的表结构, 将使用这张表来演示基础的 CRUD 接口
+ [下载源码](http://192.168.0.35:9000/hy-cloud/hy-test.zip)
:::

::: tip 关于索引
+ 索引的建立应根据实时情况而定, 例如这张测试表的条件筛选固定是需要走 scope 的左匹配, 而 user_name 则为可变性条件, 故将整合为联合索引（user_name 同样为左匹配时会生效）, 且顺序为 scope、user_name, 这样在分页时无论如何都会走索引
+ 在自定义数据权限时, 基础依赖中构建的条件为 org_id in (1, 2, 3...), 此时会有一个数据大小问题, 当传入的值比较少时, org_id 建立索引后确实会走索引, 且 type 为 range, 反之则不会
+ 所以此表结构的索引只能作为参考, 无法给到标准的答案, 具体如何建立, 一是根据实际 SQL 情况, 二还是需要自己去阅读 MySQL 在 InnoDB 引擎下索引的原理
:::

::: tip 时间与空间
+ MySQL B+ Tree 的节点大小为 16KB, 非叶子节点存储索引数据, 叶子节点存储该所在行的完整数据（或部分数据）, 在数据结构中, 查找一棵树中的某些数据的时间复杂度与树的深度有密切的关系, 而 B+ Tree 的深度与每个节点存储的数据大小有关, 故此, 合理的构建表结构有利于压缩 .ibd 文件大小, 从而提升查询性能
+ 在计算机系统中有这么两个内容可供参考, 如何维护时间与空间的平衡关系, 需要日积月累:
1. 时间局部性: 如果一个数据项正在被访问, 那么近期它可能会被再次访问, 即在相邻的时间里会访问同一个数据项
2. 空间局部性: 在最近的将来会用到的数据的地址和现在正在访问的地址很可能是相近的, 即相邻的空间地址会被连续访问
:::

+ **表结构如下**

~~~sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for test_user
-- ----------------------------
DROP TABLE IF EXISTS `test_user`;
CREATE TABLE `test_user`  (
  `id` int(8) UNSIGNED NOT NULL COMMENT '主键ID',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `sex` tinyint(2) NULL DEFAULT NULL COMMENT '性别 0：男 1：女',
  `address` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `sasac_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '国资委ID',
  `sasac_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '国资委名称',
  `group_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '集团ID',
  `group_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '集团名称',
  `company_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '公司ID',
  `company_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公司名称',
  `dept_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '部门ID',
  `dept_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门名称',
  `org_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '机构节点ID',
  `credit_code` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '社会信用代码',
  `creator_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifier_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '修改人ID',
  `modifier` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `scope` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据权限范围',
  `del` tinyint(2) NULL DEFAULT 0 COMMENT '逻辑删除; 0：未删除 1：已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_scope_user_name`(`scope`, `user_name`) USING BTREE COMMENT '数据权限、用户名'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '测试服务 -- 测试用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
~~~

+ **Java 代码如下**

1. 实体类（注意完善 Swagger 的注解内容）

~~~Java
package com.hysoft.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hysoft.datasource.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 测试服务 -- 测试用户表
 * </p>
 *
 * @author LZH
 * @since 2022-04-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value="TestUser对象", description="测试服务 -- 测试用户表")
public class TestUser extends BaseEntity<TestUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", required = true, example = "张三")
    private String userName;

    /**
     * 性别 0：男 1：女
     */
    @ApiModelProperty(value = "性别 0：男 1：女", example = "0")
    private Integer sex;

    /**
     * 家庭住址
     */
    @ApiModelProperty(value = "家庭住址", example = "重庆市渝中区牛角沱")
    private String address;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
~~~

2. 分页对象（该类为自己创建）

~~~Java
package com.hysoft.test.entity.po;

import com.hysoft.datasource.domain.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 测试用户分页对象
 *
 * @author LizhengHuang
 * @version 1.0
 * @since 2022-04-08 11:22
 */
@Data
@ApiModel(value="测试用户分页对象", description="测试服务 -- 测试用户")
public class TestUserPo extends PageEntity {

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", example = "张三")
    private String userName;

}
~~~

3. mapper.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.hysoft.test.mapper.TestUserMapper">

    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="com.hysoft.test.entity.TestUser">
        <id column="id" property="id" />
        <result column="org_id" property="orgId" />
        <result column="org_name" property="orgName" />
        <result column="credit_code" property="creditCode" />
        <result column="creator_id" property="creatorId" />
        <result column="creator" property="creator" />
        <result column="create_time" property="createTime" />
        <result column="modifier_id" property="modifierId" />
        <result column="modifier" property="modifier" />
        <result column="modify_time" property="modifyTime" />
        <result column="scope" property="scope" />
        <result column="del" property="del" />
        <result column="user_name" property="userName" />
        <result column="sex" property="sex" />
        <result column="address" property="address" />
    </resultMap>

    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        org_id,
        org_name,
        credit_code,
        creator_id,
        creator,
        create_time,
        modifier_id,
        modifier,
        modify_time,
        scope,
        del,
        id, user_name, sex, address
    </sql>

</mapper>
~~~

4. mapper 接口

~~~Java
package com.hysoft.test.mapper;

import com.hysoft.test.entity.TestUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 测试服务 -- 测试用户表 Mapper 接口
 * </p>
 *
 * @author LZH
 * @since 2022-04-08
 */
public interface TestUserMapper extends BaseMapper<TestUser> {

}
~~~

5. service 接口

~~~Java
package com.hysoft.test.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hysoft.test.entity.TestUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hysoft.test.entity.po.TestUserPo;

/**
 * <p>
 * 测试服务 -- 测试用户表 服务类
 * </p>
 *
 * @author LZH
 * @since 2022-04-08
 */
public interface TestUserService extends IService<TestUser> {

    /**
     * 测试用户列表分页
     *
     * @param po 分页对象
     * @return Page
     */
    Page<TestUser> queryList(TestUserPo po);

}
~~~

6. service 接口实现类

~~~Java
package com.hysoft.test.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hysoft.test.entity.TestUser;
import com.hysoft.test.entity.po.TestUserPo;
import com.hysoft.test.mapper.TestUserMapper;
import com.hysoft.test.service.TestUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 测试服务 -- 测试用户表 服务实现类
 * </p>
 *
 * @author LZH
 * @since 2022-04-08
 */
@Service
public class TestUserServiceImpl extends ServiceImpl<TestUserMapper, TestUser> implements TestUserService {

    /**
     * 测试用户列表分页
     *
     * <P>
     *    注意分页对象里的 {@code chip} 属性为数据权限控制值,
     *    同时建议在使用 MyBatis Plus 条件构造器时, 多利用 Lambda 形式,
     *    因为, 当你更改了实体类的成员变量后, IDEA 编辑器中能够提示错误, 而不是等到运行时抛异常
     * </P>
     *
     * @param po 分页对象
     * @return Page
     */
    @Override
    public Page<TestUser> queryList(TestUserPo po) {
        return super.page(
            new Page<>(po.getPageNum(), po.getPageSize()),
            Wrappers.<TestUser>query()
                .like(StrUtil.isNotBlank(po.getUserName()), "user_name", po.getUserName())
                .apply(po.getChip())
                .orderByDesc("id")
        );
    }
}
~~~

7. controller 控制器（注意该类的 Swagger 注解需要自己手写）

~~~Java
package com.hysoft.test.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hysoft.core.domain.ResultVo;
import com.hysoft.test.entity.TestUser;
import com.hysoft.test.entity.po.TestUserPo;
import com.hysoft.test.service.TestUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 测试服务 -- 测试用户表 前端控制器
 * </p>
 *
 * @author LZH
 * @since 2022-04-08
 */
@RestController
@RequestMapping("/test-user")
@Api(tags = "测试服务 -- 测试用户")
public class TestUserController {

    @Autowired
    private TestUserService service;

    /**
     * 新增测试用户信息
     *
     * @param user 测试用户对象
     * @return ResultVo
     */
    @ApiOperation(
            value = "新增测试用户信息",
            httpMethod = "POST"
    )
    @Validated
    @PostMapping
    public ResultVo<?> add(@RequestBody TestUser user) {
        return service.save(user) ? ResultVo.success() : ResultVo.error();
    }

    /**
     * 根据主键 ID 逻辑删除用户信息
     *
     * @param id 主键 ID
     * @return ResultVo
     */
    @ApiOperation(
            value = "根据主键 ID 逻辑删除用户信息",
            httpMethod = "DELETE"
    )
    @DeleteMapping("{id}")
    public ResultVo<?> del(@ApiParam(value = "主键ID", required = true, example = "1")
                           @PathVariable @NotNull Integer id) {
        return service.removeById(id) ? ResultVo.success() : ResultVo.error();
    }

    /**
     * 根据主键 ID 修改用户信息
     *
     * @param user 测试用户对象
     * @return ResultVo
     */
    @ApiOperation(
            value = "根据主键 ID 修改用户信息",
            httpMethod = "PUT"
    )
    @Validated
    @PutMapping
    public ResultVo<?> update(@RequestBody TestUser user) {
        return service.updateById(user) ? ResultVo.success() : ResultVo.error();
    }

    /**
     * 根据主键 ID 查询用户信息
     *
     * @param id 主键 ID
     * @return ResultVo
     */
    @ApiOperation(
            value = "根据主键 ID 查询用户信息",
            httpMethod = "GET"
    )
    @GetMapping("{id}")
    public ResultVo<TestUser> getById(@ApiParam(value = "主键ID", required = true, example = "1")
                                     @PathVariable @NotNull Integer id) {
        return ResultVo.success(service.getById(id));
    }

    /**
     * 测试用户列表分页
     *
     * @param po 分页对象
     * @return ResultVo
     */
    @ApiOperation(
            value = "测试用户列表分页",
            httpMethod = "GET"
    )
    @GetMapping("queryList")
    public ResultVo<Page<TestUser>> queryList(TestUserPo po) {
        return ResultVo.success(service.queryList(po));
    }
}
~~~

8. bootstrap.yml

~~~yml
# 开发环境 - （测试项目）配置文件

# Tomcat
server:
  port: 8080

# Spring
spring:
  # 应用名称（全局唯一）
  application:
    name: hy-test
  cloud:
    # 配置局域网网段
    inetutils:
      preferred-networks: 192.168.0
    # Nacos 配置
    nacos:
      # 服务注册地址
      discovery:
        server-addr: 192.168.0.120:8848
        namespace:
      config:
        # 命名空间（开发环境）
        namespace: dev
        # 配置中心地址
        server-addr: 192.168.0.120:8848
        # 最大连接错误数
        max-retry: 100
        # 配置文件格式
        file-extension: yml
        # 通用配置（唯一标识、分组标识、是否动态刷新）
        # 注意分组标识, 当一个业务有多个服务时, 分组标识应该一致
        # 例如平台拆分的粒度细分为用户服务、角色服务..., 而不单单只是模块时, 分组标识应都为 system
        shared-configs:
          - data-id: hy-common.yml
            group: common
            refresh: true
          - data-id: hy-redis.yml
            group: common
            refresh: true
          - data-id: hy-rocketmq.yml
            group: common
            refresh: true
          - data-id: hy-test.yml
            group: test
            refresh: true
~~~

::: danger 总结
MyBatis-Plus 帮我们完成许多与数据库打交道的业务功能, 善用 Mapper、 Service 接口的父类方法, 将简化我们的代码同时提升编码效率, 但有时候往往我们需要考虑到性能和与前端交互的问题, 例如查询的字段不能是全字段、多表关联、入参参数不为整个实体类时, 我们该考虑使用 vo、ro、dto 等交互对象完成, 当超过三个表关联或表数据较大或 SQL 复杂时, 我们应该考虑手写 SQL 或将 SQL 拆分成多个单表查询的 SQL, 结合 Java 集合的特性来降低线程栈执行时的时间复杂度和空间复杂度
:::