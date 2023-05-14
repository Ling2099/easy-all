# 系统基础 API

::: tip 提示
系统基础服务提供的 HTTP API 接口均以打进核心包类（已被 Spring 容器管理, 直接注入使用），且已完成服务降级功能，无需再造轮子<b>（持续更新）</b>
:::

<table>
  <tr>
    <td>接口名</td>
	<td>方法名</td>
	<td>参数类型</td>
	<td>入参参数</td>
	<td>返回类型</td>
	<td>说明</td>
  </tr>
  <tr>
    <td>RemoteFileService</td>
	<td>getFileTemplate</td>
	<td>String</td>
	<td>fileUrl</td>
	<td>ResultVo&lt;byte[]&gt;</td>
	<td>获取模板文件的字节数组, 用于模板下载 HTTP 服务间接口调用</td>
  </tr>
  <tr>
    <td>待完善</td>
	<td>待完善</td>
	<td>待完善</td>
	<td>待完善</td>
	<td>待完善</td>
	<td>待完善</td>
  </tr>
</table>