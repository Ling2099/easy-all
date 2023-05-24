### 基础依赖

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

#### &#x1F527; 响应工具
---
::: tip 说明
封装了基础的 web 接口响应方法, 且实现单例, 避免频繁被创建
:::

~~~java
@GetMapping
public ResultVo<List<Integer>> getList() {
    return ResultVo.ok(Arrays.asList(1, 2, 3));
}
~~~

&#x1F36D; 响应属性及方法

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

&#x1F368; 响应状态码和对应的响应信息（StatusEnum）

| 状态码编号 | 响应信息 |
| ---- | ---- |
| 200 | 操作成功 |
| 401 | 未授权 |
| 403 | 访问受限，授权过期 |
| 404 | 资源未找到 |
| 423 | 您的账号已从其它设备登录, 请注意隐私保护 |
| 429 | 操作过于频繁, 请稍后再试 |
| 500 | 系统内部错误 |

#### 基础异常
---
::: tip 说明
简单的基础异常类, 可以配合 easy-web 中的全局异常捕获
:::

~~~java
public void append(String str) {
    if (str == null) {
        throw new BaseException("字符串不能为空");
    }
}
~~~

#### 线程工具
---
::: tip 说明
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