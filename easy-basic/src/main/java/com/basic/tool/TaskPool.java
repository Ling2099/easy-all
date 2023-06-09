package com.basic.tool;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 异步任务、定时任务、延迟任务队列配置工具类
 *
 * <p>异步任务:</p>
 * <ol>
 *     <li>{@link #execute(Runnable)}: 无返回值的异步线程</li>
 *     <li>{@link #submit(Callable)}: 有返回值的异步线程</li>
 * </ol>
 *
 *
 * <p>延迟队列:</p>
 * <ol>
 *     <li>{@link #push(long, Object, TimeUnit)}: 向延迟队列中添加元素</li>
 *     <li>{@link #pull()}: 取出延迟队列中的元素</li>
 * </ol>
 *
 *
 * <p>定时任务:</p>
 * <ol>
 *     <li>{@link #plan(Runnable, String, String, long, TimeUnit)}: 指定首次延时执行时间点, 后续根据频率执行</li>
 *     <li>{@link #plan(Runnable, String, long, long, TimeUnit)}: 指定首次延时执行时长, 后续根据频率执行</li>
 *     <li>{@link #cancel(String)}: 取消定时任务</li>
 * </ol>
 *
 *
 * <p>{@link #closeScheduled()}: 关闭定时任务线程池</p>
 * <p>{@link #closeThreadPool()}: 关闭异步任务线程池</p>
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class TaskPool {

    /**
     * 日志记录: {@link Logger}
     */
    private static final Logger log = LoggerFactory.getLogger(TaskPool.class);

    /**
     * 换算为毫秒的时间长度
     *
     * <ul>
     *     <li>天: 24 * 60 * 60 * 1000</li>
     *     <li>时: 60 * 60 * 1000</li>
     *     <li>分: 60 * 1000</li>
     *     <li>秒: 1000</li>
     * </ul>
     */
    private static final long DAY_MILLIS = 86400000,
            HOURS_MILLIS = 3600000,
            MINUTES_MILLIS = 60000,
            SECONDS_SECONDS = 1000;

    /**
     * Private Constructor
     */
    private TaskPool() {}

    /**
     * 返回可用处理器的 Java 虚拟机的数量, 同时也利用此参数作为线程池的 <b style="color:blue">核心数、最大线程数</b>
     *
     * <hr/>
     *
     * <p>
     *     由于无法观察到实际服务器情况（CPU 密集型或 I/O 密集型）, 也即无法计算阻塞系数,<br/>
     *     遂根据 CPU 一核一线程的伪数量, 得到可用的线程数（假想值）,且不跟随帖子、教程将其乘 2 操作, <br/>
     *     因为纵观整个项目里使用到自定义异步线程的地方屈指可数, 过多的核心数、最大线程数, 只会在执行任务时不断的创建新的线程 <b style="color:red">(见源码)</b>,
     *     达不到多线程复用的特性, 使阻塞队列形同虚设, 从而增加内存开销, 更有甚者导致卡顿、内存泄露等现象. <br/>
     * </p>
     *
     * <hr/>
     *
     * <P>
     *     对于一般性的程序（项目）而言, 流水账式的编码足以应付大多数场景（CRUD）, 其中业务逻辑、使用能力与硬件配置占了大部分比例,
     *     所以其实几个自定义线程数量足够了
     * </P>
     *
     * <hr/>
     *
     * <p>理论公式: N(thread) = N(cpu) * U(cpu) * (1 + W/C)</p>
     * <ol>
     *     <li><b style="color:red">N(thread):</b> 计算得到的线程数</li>
     *     <li><b style="color:red">N(cpu):</b>    CPU 的核心数, 例如单核 1 颗 CPU</li>
     *     <li><b style="color:red">U(cpu):</b>    期望的 CPU 的利用率, 例如期望 CPU 的工作效率为 70%</li>
     *     <li><b style="color:red">W:</b>         CPU 的等待时间, 例如执行一段任务时, 有百分之 50% 的时间是等待状态</li>
     *     <li><b style="color:red">C:</b>         CPU 的计算时间, 例如执行一段任务时, 有百分之 50% 的时间是工作状态</li>
     *     <p>所以, 上述介绍的例子中, 线程数 = 1 * 70% * （1 + 50% / 50%）</p>
     * </ol>
     *
     * <hr/>
     *
     * <b>但公式只是理论值, 想要得到具体且合理的线程数, 需在生产环境中不断的调试、观察, 以得到最终的参数值</b>
     *
     * <p>很遗憾, 就目前而言, 无法做到</p>
     */
    private static final int NUM = Runtime.getRuntime().availableProcessors();

    /**
     * 定时任务记录容器, 用于保存/取消定时任务
     *
     * <p>
     *     当然, {@link Future#cancel(boolean)} 接口方法只是试图取消一个线程的执行,
     *     但并不一定能取消成功, 因为任务可能已完成、已取消或者其它因素不能取消, 存在取消失败的可能.
     * </p>
     *
     * <hr/>
     *
     * <p>
     *     {@link Map} 容器的 key、value 如下:
     *     <ul>
     *         <li>key: 客户端区分不同定时任务的唯一自定义标识</li>
     *         <li>value: 当前指定、计划、执行的定时任务接口 {@link ScheduledFuture}</li>
     *     </ul>
     * </p>
     */
    private static final Map<String, ScheduledFuture<?>> TASK = new ConcurrentHashMap<>(NUM << 1);

    /**
     * 自定义延迟队列
     *
     * <p>使用场景:</p>
     * <ol>
     *     <li>模块解耦</li>
     *     <li>任务有时间延迟限制</li>
     *     <li>单机版消息队列</li>
     * </ol>
     */
    private static final DelayQueue<Item<?>> QUEUE = new DelayQueue<>();

    /**
     * 自定义异步线程池
     *
     * <p>用于执行客户端的调度任务, 保证线程复用, 提高程序的生产力, 节约响应时间</p>
     *
     * @see LinkedBlockingDeque
     * @see NamedThreadFactory
     * @see Rejected
     */
    private static final ExecutorService POOL = new ThreadPoolExecutor(
            // 线程的数量
            NUM,
            // 最大线程数量
            NUM,
            // 线程存活时间
            60,
            // 时间单位
            TimeUnit.SECONDS,
            // 阻塞队列
            new LinkedBlockingDeque<>(),
            // 自定义创建线程的工厂
            new NamedThreadFactory(),
            // 自定义拒绝策略
            new Rejected()
    );

    /**
     * 自定义定时任务
     *
     * <p>
     *     用于执行客户端需要周期性的线程任务, <br/>
     *     <b style="color:red">特别注意的是: </b>{@link ScheduledThreadPoolExecutor} 的构造方法并没有最大线程数的参数, 而是使用
     *     {@link Integer#MAX_VALUE} 的低 29 位, 说明其可以无限制的开启任意线程执行任务, 造成 OOM.
     * </p>
     *
     * <P style="color:yellow">所以在大量任务系统, 应注意使用</P>
     *
     * @see NamedThreadFactory
     * @see Rejected
     */
    private static final ScheduledExecutorService SCHEDULED = new ScheduledThreadPoolExecutor(
            // 线程的数量
            NUM,
            // 自定义创建线程的工厂
            new NamedThreadFactory(),
            // 自定义拒绝策略
            new Rejected()
    );

    /**
     * 执行线程任务且无返回值, 也可执行一次计划任务（定时任务）
     *
     * <p style="color:red">
     *     需要额外注意的是, {@link Executor#execute(Runnable)} 无法捕获执行时异常
     * </p>
     *
     * <p>如下例子:</p>
     *
     * <pre>{@code
     * public class Example {
     *     // 例1
     *     public void print() {
     *         TaskPool.execute(() -> System.out.println("我是没有返回值的"));
     *         TaskPool.close();
     *     }
     *
     *     // 例2
     *     public void div() {
     *         TaskPool.execute(
     *             try {
     *                 int num = 1 / 0;
     *             } catch (ArithmeticException e) {
     *                 log.error("Thread Exception:", e);
     *             }
     *         );
     *         TaskPool.close();
     *     }
     * }
     * }</pre>
     *
     * @param command {@link Runnable} 接口实例
     */
    public static void execute(Runnable command) {
        POOL.execute(command);
    }

    /**
     * 带返回值执行线程任务
     *
     * <p>也可用于计划任务（定时任务）的执行一次业务</p>
     *
     * <pre>{@code
     *     public class Example {
     *         public void add() {
     *             Future<Integer> future = TaskPool.submit(() -> 1 + 1);
     *
     *             try {
     *                 System.out.println(future.get());
     *             } catch (InterruptedException | ExecutionException e) {
     *                 log.error("Thread Exception:", e);
     *             }
     *
     *             TaskPool.close();
     *         }
     *     }
     * }</pre>
     *
     * @param task 实现了 {@link Callable} 线程的任务
     * @param <V>  泛型
     * @return {@link Future} 异步阻塞接口
     */
    public static <V> Future<V> submit(Callable<V> task) {
        return POOL.submit(task);
    }

    /**
     * 等待线程任务执行完成, 并获取其返回值
     *
     * <p>在 {@link #submit(Callable)} 执行完后可获取其值</p>
     *
     * <pre>{@code
     *     public class Example {
     *         public void test() {
     *             Future<Integer> future = TaskPool.submit(() -> {
     *                 for (int i = 0; i < 10; i++) {
     *                     System.out.println(i);
     *                     Thread.sleep(2000);
     *                 }
     *                 return 100;
     *             });
     *
     *             System.out.println("我是主线程");
     *
     *             // 这里会阻塞等待
     *             Integer num = TaskPool.get(future);
     *             System.out.println(num);
     *         }
     *     }
     * }</pre>
     *
     * @param future {@link Future}
     * @param <R>    泛型
     * @return R
     */
    public static <R> R get(Future<R> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Thread Exception: ", e);
        }
        return null;
    }

    /**
     * 向延迟队列中添加待执行的元素（数据）
     *
     * <pre>{@code
     *     public class Example {
     *
     *         public Example() {
     *             while (true) {
     *                 String str = TaskPool.pull();
     *                 System.out.println(str);
     *             }
     *         }
     *
     *         public static void producer() {
     *             // 模拟往队列中添加元素
     *             for (int i = 0; i < 100; i++) {
     *                 TaskPool.push(5, "我是数据" + i, TimeUnit.MINUTES);
     *             }
     *         }
     *     }
     * }</pre>
     *
     * @param activeTime 过期时间
     * @param t          队列元素
     * @param unit       时间单位
     * @param <T>        泛型
     */
    public static <T> void push(long activeTime, T t, TimeUnit unit) {
        QUEUE.offer(new Item(activeTime, t, unit));
    }

    /**
     * 取出延迟队列中的元素
     *
     * <pre>{@code
     *     public class Example {
     *         public Example() {
     *             while (true) {
     *                 String str = TaskPool.pull();
     *                 System.out.println(str);
     *             }
     *         }
     *
     *         public static void producer() {
     *             // 模拟往队列中添加元素
     *             for (int i = 0; i < 100; i++) {
     *                 TaskPool.push(5, "我是数据" + i, TimeUnit.MINUTES);
     *             }
     *         }
     *     }
     * }</pre>
     *
     * @param <T> 泛型
     * @return 队列中的元素
     */
    public static <T> T pull() {
        try {
            return submit(() -> (T) QUEUE.take().getT()).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Thread Exception: ", e);
        }
        return null;
    }

    /**
     * 定时任务
     *
     * <P>自定义首次延时时长和执行频率</P>
     *
     * <pre>{@code
     *     // 以当前时间为基准, 延时至 1 小时后执行一次, 后续每 2 小时执行一次
     *     TaskPool.plan(() -> System.out.println("吃根烟先!"), "unique", 1, 2, TimeUnit.HOURS);
     *
     *     // 立马执行, 后续每 5 分钟执行一次
     *     TaskPool.plan(() -> System.out.println("立马执行哈!"), "unique", 0, 5, TimeUnit.MINUTES);
     * }</pre>
     *
     * <b style="color:red">会加入 {@link #TASK} 容器, 方便暂停/启动操作</b>
     *
     * @param command {@link Runnable} 接口实例
     * @param key     该任务的唯一标识（客户端自定义）, 用于区分不同的定时任务
     * @param delay   首次执行延时时长
     * @param period  首次执行后, 后面定时任务的执行频率
     * @param timer   {@link TimeUnit} 时间单位
     */
    public static void plan(Runnable command, String key,
                            long delay, long period, TimeUnit timer) {
        ScheduledFuture<?> future = SCHEDULED.scheduleAtFixedRate(command, delay, period, timer);
        TASK.put(key, future);
    }

    /**
     * 定时任务
     *
     * <p style="color:red">时间单位: 天、时、分、秒</p>
     *
     * <pre>{@code
     *     // 首次执行于 2099-01-01 00:00:00, 往后每 30 天执行一次
     *     TaskPool.plan(
     *         () -> System.out.println("三体人来了吗?"),
     *         "unique",
     *         "2099-01-01 00:00:00",
     *         30,
     *         TimeUnit.DAYS
     *     );
     *
     *     // 如果设置的延时时间点小于当前时间, 则定时任务立马执行, 往后同样的每个周期执行一次
     *     TaskPool.plan(
     *         () -> System.out.println("发生了什么?"),
     *         "unique",
     *         "1989-06-04 01:00:00",
     *         1,
     *         TimeUnit.DAYS
     *     );
     * }</pre>
     *
     * @param command {@link Runnable} 接口实例
     * @param key     该任务的唯一标识（客户端自定义）, 用于区分不同的定时任务
     * @param day     指定的首次延时时间点, <b style="color:red">格式为 yyyy-MM-dd HH:mm:ss</b>
     * @param period  首次执行后, 后面定时任务的执行频率
     * @param timer   {@link TimeUnit} 时间单位, <b style="color:red">需要注意, 只支持天、时、分、秒</b>
     */
    public static void plan(Runnable command, String key, String day, long period, TimeUnit timer) {
        switch (timer) {
            case DAYS    : period = Math.multiplyExact(period, DAY_MILLIS); break;
            case HOURS   : period = Math.multiplyExact(period, HOURS_MILLIS); break;
            case MINUTES : period = Math.multiplyExact(period, MINUTES_MILLIS); break;
            case SECONDS : period = Math.multiplyExact(period, SECONDS_SECONDS); break;
            default      : period = 3000;
        }
        plan(command, key, getMillis(day, period), period, TimeUnit.MILLISECONDS);
    }

    /**
     * 取消定时任务
     *
     * <p>可能会取消失败, 详情描述在 {@link #TASK} 注释中</p>
     *
     * @param key 该任务的唯一标识
     */
    public static void cancel(String key) {
        TASK.computeIfPresent(key, (k, v) -> {
            v.cancel(true);
            TASK.remove(k);
            return v;
        });
    }

    /**
     * 关闭线程池, 进入过渡状态
     *
     * <p>此时还是会执行队列中的任务, 但不接受新的任务</p>
     */
    public static void closeThreadPool() {
        POOL.shutdown();
    }

    /**
     * 关闭定时任务线程池, 进入过渡状态
     *
     * <p>此时还是会执行队列中的任务, 但不接受新的任务</p>
     */
    public static void closeScheduled() {
        SCHEDULED.shutdown();
    }

    /**
     * 计算定时任务中, 首次延时执行的时长（毫秒）
     *
     * @param day    给定的目标延时时间点, 如: <i style="color:red">2023-10-01 14:00:33</i>
     * @param period 定时任务执行的频率（已换算为毫秒）
     * @return 首次延时时长
     */
    private static long getMillis(String day, long period) {
        long delay = LocalDateTime.parse(day, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .toInstant(ZoneOffset.of("+8"))
                .toEpochMilli();
        return (delay = delay - System.currentTimeMillis()) > 0 ? delay : period + delay;
    }

    /**
     * 自定义线程的批量创建工厂
     *
     * <ol>
     *     <li>自定义线程名称</li>
     *     <li>自定义是否为守护线程</li>
     *     <li>线程优先级</li>
     * </ol>
     */
    private static class NamedThreadFactory implements ThreadFactory {

        /**
         * 原子性的递增线程数量
         */
        private final AtomicInteger threadNumber;

        /**
         * 当前被创建的线程的线程组
         */
        private final ThreadGroup group;

        /**
         * 无参构造函数
         */
        @SuppressWarnings("removal")
        private NamedThreadFactory() {
            // 原子性的递增线程数量, 步长为 1
            this.threadNumber = new AtomicInteger(1);

            // 获取系统安全接口
            SecurityManager s = System.getSecurityManager();
            // 判断使用系统获取的线程组还是当前线程的线程组
            this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        /**
         * 创建线程的策略
         *
         * @param run 异步任务
         * @return {@link Thread} 当前创建的线程
         */
        @Override
        public Thread newThread(Runnable run) {
            // 创建执行线程, 线程名为 Custom + 进行递增序号
            Thread thread = new Thread(group, run, "Custom" + this.threadNumber.getAndIncrement());
            // 设置为非守护线程
            thread.setDaemon(false);
            // 设置线程的优先级, Java 根据操作系统的不同, 分布有 1 ~ 10 的优先级
            // 且不同的操作系统优先级大相径庭, 如有的操作系统优先级为: 低、中、高
            // 但这里的优先级只是给 CPU 提供执行建议, 最终的执行权由 CPU 解释
            thread.setPriority(Thread.MAX_PRIORITY);
            return thread;
        }
    }

    /**
     * 自定义线程拒绝处理策略
     *
     * <P>当线程数量大于最大线程数时执行此拒绝策略</P>
     */
    private static class Rejected implements RejectedExecutionHandler {

        /**
         * 自定义拒绝策略
         *
         * @param r        当前被拒绝的任务
         * @param executor 当前的线程池
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // 获取当前线程的阻塞队列
            final BlockingQueue<Runnable> queue = executor.getQueue();
            try {
                // 将该任务加入队列队尾, 若队列已满, 则等待 1 分钟
                // noinspection ResultOfMethodCallIgnored
                queue.offer(r, 1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                log.error("线程丢弃时, 执行拒绝策略错误: ", e.getMessage());
            }
        }
    }

    /**
     * 实现了 {@link Delayed} 延迟队列接口的实现类
     *
     * <p>用于存储过期时间、时间单位、数据元素</p>
     *
     * @param <T> 泛型
     */
    private static class Item<T> implements Delayed {

        /**
         * 过期时间
         */
        private final long activeTime;

        /**
         * 待消费的元素
         */
        private final T t;

        /**
         * 初始化构造函数
         *
         * @param activeTime 过期时间
         * @param t          数据元素
         * @param unit       时间单位
         */
        private Item(long activeTime, T t, TimeUnit unit) {
            this.activeTime = unit.convert(activeTime, unit) + System.nanoTime();
            this.t = t;
        }

        /**
         * 获取延迟时间
         *
         * @param unit 时间单位
         * @return 延迟的时长
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(activeTime - System.nanoTime(), unit);
        }

        /**
         * 继承自接口 {@link Comparable#compareTo(Object)},
         * 用于比较指定对象的顺序, 也即大小
         *
         * @param o {@link Delayed}
         * @return int
         */
        @Override
        public int compareTo(Delayed o) {
            long diff = getDelay(TimeUnit.NANOSECONDS) - getDelay(TimeUnit.NANOSECONDS);
            return diff == 0 ? 0 : (diff > 0 ? 1 : -1);
        }

        /**
         * 待延迟时间到达, 获取被阻塞的元素数据
         *
         * @return T
         */
        private T getT() {
            return t;
        }
    }

}
