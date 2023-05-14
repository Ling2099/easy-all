# 任务调度

---

::: tip 提示
做这个服务的目的是统一管理 Spring 定时任务线程池，将计时的功能托管给固定的服务， 业务系统不再写死 cron 表达式或造轮子，且能够灵活的可视化启动/暂停任务。
:::

::: warning 注意
此模块为非必需依赖，当你项目中压根就没有定时任务这一业务时，可以选择排除掉，如下
:::

~~~Xml
<dependency>
    <groupId>com.hysoft</groupId>
    <artifactId>hy-common-core</artifactId>
    <exclusions>
        <exclusion>
            <groupId>com.hysoft</groupId>
            <artifactId>hy-common-job</artifactId>
        </exclusion>
    </exclusions>
</dependency>
~~~

+ 要实现定时任务，只需要实现 **com.hysoft.job.service.TaskService** 接口的 **handle** 方法即可，并添加配置，如下示例

~~~Java
/**
 * 记得将该对象注入 Spring 容器
 */
@Service
public class UserServiceImpl implements TaskService {

    /**
     * 由于被 Spring 容器所管理, 你也可以轻松的注入其它 Bean 对象以使用;
     *
     * <b>
     *     但需要注意的是, 该定时任务无用户状态, 在写数据库时,
     *     避免夹带 Mybatis Plus 的自动填充字段而使程序报错
     * </b>
     */
    @Override
    public void handle() {
        // 你的业务逻辑代码
    }

}
~~~

+ 在客户端添加任务配置

![avatar](/assets/img/hy-task.png)