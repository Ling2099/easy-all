### web 整合

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

::: tip 说明
此组件中默认包含了 easy-basic 和 easy-orm 组件的所有内容, 皆为打造低代码的 web 项目手段。
:::

#### 全局异常捕获

通过在项目配置文件中进行开启, 以获得全局系统异常、自定义基础异常、参数校验异常的捕获, 如下

~~~yml
easy:
  exception: true
~~~

另外, 你还可以实现扩展接口来将异常信息自定义操作, 如持久化至数据库、文件、消息队列等

~~~java
@Service
public class YourClass implements LogExtend {

  @Override
  public void log(Exception ex) {
      // 你的具体逻辑
  }

  @Override
  public void log(BaseException ex) {
      // 你的具体逻辑
  }

  @Override
  public void log(MethodArgumentNotValidException ex) {
      // 你的具体逻辑
  }

}
~~~

#### 消息头拦截

通过在项目配置文件中进行开启, 获得请求时的消息头拦截, 并自动写入本地线程副本（ThreadLocal）中; 相应的, 你也可以配置不被拦截器处理的 url 地址, 如下

~~~yml
easy:
  filter: true
  # 不会被拦截的地址
  url:
    - /login
    - /logout
    - /other
~~~

#### 简化的注解

+ 对 @RestController 和 RequestMapping 进行合并处理

~~~java
@RestMapping("user")
public class UserController {

}
~~~

+ 针对 SpringBoot 启动注解进行了整合处理, 默认开启了异步支持、启动注解

~~~java
@StartAnnotations
public class DemoApplication {
    
    public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
~~~