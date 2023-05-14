---
title: 服务简介
---

## 平台结构
~~~
├─hy-auth                  认证授权服务
├─hy-common
│  ├─hy-common-alibaba     Spring Cloud Alibaba 相关依赖模块
│  ├─hy-common-core        系统核心代码模块
│  ├─hy-common-datascope   数据权限范围模块
│  ├─hy-common-datasource  数据库相关配置模块
│  ├─hy-common-jpush       APP预留极光推送模块
│  ├─hy-common-jwt         系统认证模块
│  ├─hy-common-models      系统公共模型模块
│  ├─hy-common-redis       非关系型数据库模块
│  ├─hy-common-rocketmq    消息队列模块
│  ├─hy-common-security    系统内部校验与数据传递模块
│  └─hy-common-swagger     前后分离API接口文档模块
├─hy-gateway               系统网关
├─hy-netty                 系统长链接服务
├─hy-portal
│  ├─hy-portal-file        文件服务
│  ├─hy-portal-flow        流程服务
│  ├─hy-portal-home        门户服务
│  ├─hy-portal-log         日志服务
│  └─hy-portal-system      系统基础服务
├─sql                      平台备份 SQL
└─微服务文档
~~~

## 相关技术
<table>
  <tr align="center">
    <td>分类</td>
	<td>名称</td>
	<td>版本</td>
	<td>基础</td>
	<td>进阶</td>
  </tr>
  <tr align="center">
    <td>编程语言</td>
	<td>Java</td>
	<td>1.8</td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
    <td rowspan="3">核心框架</td>
	<td>SpringBoot</td>
	<td>2.6.3</td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
	<td>SpringCloud</td>
	<td>2021.0.0</td>
	<td></td>
	<td>√</td>
  </tr>
  <tr align="center">
    <td>SpringCloudAlibaba</td>
	<td>2021.1</td>
	<td></td>
	<td>√</td>
  </tr>
  <tr align="center">
    <td>项目管理</td>
	<td>Maven</td>
	<td>3.x.x</td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
    <td>代码托管</td>
	<td>Git</td>
	<td>v1.9.2</td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
    <td>私服仓库</td>
	<td>Nexus</td>
	<td></td>
	<td></td>
	<td>√</td>
  </tr>
  <tr align="center">
    <td>注册中心</td>
	<td>Nacos</td>
	<td>2.0.4</td>
	<td></td>
	<td>√</td>
  </tr>
  <tr align="center">
    <td>非关系型数据库</td>
	<td>Redis</td>
	<td></td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
    <td rowspan="3">关系型数据库</td>
	<td>MySQL</td>
	<td>8.0.x</td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
	<td>Canal</td>
	<td>v1.1.1</td>
	<td></td>
	<td>√</td>
  </tr>
  <tr align="center">
    <td>Druid</td>
    <td>1.1.22</td>
	<td></td>
	<td>√</td>
  </tr>
  <tr align="center">
    <td>ORM框架</td>
	<td>MyBatis-Plus</td>
	<td>3.4.0</td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
    <td rowspan="2">第三方插件</td>
	<td>Hutool</td>
	<td>5.7.18</td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
	<td>Lombok</td>
	<td>1.18.12</td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
    <td>JSON处理</td>
	<td>FastJson</td>
	<td>1.2.73</td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
    <td>安全框架</td>
	<td>JWT</td>
	<td>0.10.5</td>
	<td></td>
	<td>√</td>
  </tr>
  <tr align="center">
    <td>API接口文档</td>
	<td>Swagger</td>
	<td>3.0.0</td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
    <td>文件系统</td>
	<td>MinIO</td>
	<td>8.0.3</td>
	<td></td>
	<td>√</td>
  </tr>
  <tr align="center">
    <td>消息队列</td>
	<td>RocketMQ</td>
	<td>2.1.0</td>
	<td></td>
	<td>√</td>
  </tr>
  <tr align="center">
    <td>日志系统</td>
	<td>ELK</td>
	<td></td>
	<td></td>
	<td>√</td>
  </tr>
  <tr align="center">
    <td rowspan="2">文档处理</td>
	<td>EasyExcel</td>
	<td></td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
	<td>POI</td>
	<td></td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
    <td>属性校验</td>
	<td>Hibernate Validator</td>
	<td></td>
	<td>√</td>
	<td></td>
  </tr>
  <tr align="center">
    <td>分布式事务</td>
	<td>Seata</td>
	<td></td>
	<td></td>
	<td>√</td>
  </tr>
</table>

## 请求过程

![avatar](/assets/img/hy-img.png)

+ 平台支持以用户实际IP地址生成Token
+ 用户实际IP作为Redis键（Key）存储用户信息，包括用户基本信息、菜单按钮权限、数据权限
+ 前端通过国密SM2加密密文传输用户密码
+ 网关拦截用户后续请求，截取消息头的Token，解密后获得IP地址，并与当前IP进行校验
+ 校验通过后，从Redis中获取用户信息，并再次封装进后续路由的Request请求头中
+ 各业务系统利用拦截器获取请求头所携带的各项数据，包装进本地线程副本变量中，以便使用

### 数据权限
+ 平台支持动态代理方式截取 <b>com.hysoft.**.service.impl</b> 包下所有方法前缀为 query、find 的方法入参参数
+ 自动从本地线程副本中获取当前用户的数据权限，并拼接出SQL 条件
+ 设置回方法的入参参数，各业务系统利用参数值自定义SQL语句的条件判断
+ 相关注意事项及使用指南在 [数据权限](#313) 中叙述

### 服务调用
+ 平台核心包中已整合进 Spring Cloud Feign 组件，通过编写客户端API接口及实现服务降级的 FallBack 类提供对外服务间的调用接口
+ 相关注意事项在 [系统基础 API](#303) 中叙述