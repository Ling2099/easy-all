# 服务调用

::: tip 提示
Spring Cloud 中服务间的调用基于 HTTP 协议，获取注册中心的 IP、port，支持 Restful 风格的开发方式，理解方便且易用；如下是接口提供/调用方的示例
:::

+ 接口提供方示例

~~~Java
/**
 * 正常的在 controller 层编写业务接口即可
 * 注意返回值使用 ResultVo 类尽量包装 Map、List 这样的集合、基本数据类型或 String、JSON 字符串
 * 若使用引用类型, 例如 User 对象, 则需要与调用方约定好, 因为对方程序里可能会没有你的 User 类
 */
@GetMapping("login/{userName}/{passWord}")
public ResultVo<Map<String, Object>> login(@PathVariable String userName, @PathVariable String passWord) {
	return ResultVo.success(service.login(userName, passWord));
}
~~~

+ 接口调用方示例

~~~Java
/**
 * 编写远程 Feign 客户端接口
 *
 * 注解 @FeignClient 属性解释：
 * contextId: 上下文ID, 与接口名一致即可, 注意大小写
 * value: 接口提供方的服务名称, 例如这里调用的是 hy-system 服务的某个接口，
 *        注册中心上所展示的也是名为 hy-system 的服务，只是这里我使用的常量类而已
 * fallbackFactory: 服务降级类
 */
@FeignClient(
        contextId = "remoteSystemService",
        value = ServiceConstants.HY_SYSTEM,
        fallbackFactory = RemoteSystemFallBackFactory.class
)
public interface RemoteSystemService {

	/**
     * 系统登录远程调用接口
     *
     * @param username 账户
     * @param passWord 密码
     * @return ResultVo
     */
    @GetMapping("/sys-user/login/{userName}/{passWord}")
    ResultVo<Map<String, Object>> login(@PathVariable("userName") String username,
                                        @PathVariable("passWord") String passWord);
}
~~~

~~~Java
/**
 * 编写远程 Feign 客户端接口的服务降级类
 * 注意所响应的内容应根据实际场景构造
 */
@Component
public class RemoteSystemFallBackFactory implements FallbackFactory<RemoteSystemService> {

	/**
	 * 服务降级实现
	 *
	 * @param cause 异常
	 * @return RemoteSystemService
	 */
	@Override
	public RemoteSystemService create(Throwable cause) {
	    return (username, passWord) -> ResultVo.error("登录失败: " + cause.getMessage());
	}
}
~~~