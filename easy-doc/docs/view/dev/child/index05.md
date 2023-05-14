# Restful 接口

::: tip 提示
+ 什么是 Restful 接口？准确的说它是一种接口编写的风格，遵循这种风格可以使我们的代码更简介，可读性更强
+ 一般性的项目接口使用经典的 POST、GET、PUT、DELETE 就差不多够了
+ 下面是一些示例, 需要注意的是
:::

<table>
  <tr>
    <td>POST</td>
	<td>用于请求服务器提交数据</td>
  </tr>
  <tr>
    <td>GET</td>
	<td>用于请求服务器返回数据</td>
  </tr>
  <tr>
    <td>PUT</td>
	<td>用于需要修改服务器数据</td>
  </tr>
  <tr>
    <td>DELETE</td>
	<td>用于请求服务器删除数据</td>
  </tr>
</table>

```Java
/**
 * 万能的 User 对象
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@PostMapping
	public ResultVo<?> add(@RequestBody UserRo ro) {}

	@DeleteMapping("{id}")
	public ResultVo<?> del(@PathVariable Integer id) {}

	@PutMapping
	public ResultVo<?> update(@RequestBody UserRo ro) {}

	@GetMapping("{id}")
	public ResultVo<UserVo> getUserById(@PathVariable Integer id) {}

	@GetMapping("queryUser")
	public ResultVo<UserVo> queryUser(UserPo po) {}
}
```

或者像这样

```Java
/**
 * 万能的 User 对象
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@PostMapping("add")
	public ResultVo<?> add(@RequestBody UserPo ro) {}

	@DeleteMapping("del/{id}")
	public ResultVo<?> del(@PathVariable Integer id) {}

	@PutMapping("update")
	public ResultVo<?> update(@RequestBody UserPo ro) {}

	@GetMapping("getUserById/{id}")
	public ResultVo<UserVo> getUserById(@PathVariable Integer id) {}

	@GetMapping("queryUser")
	public ResultVo<UserVo> queryUser(UserPo ro) {}
}
```