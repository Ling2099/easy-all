# 版本命名

::: tip 提示
主要是解决每次项目软件修改时提供版本命名规范, 根据版本号方便追溯项目软件的更新历史记录和获得正确版本的软件, 是项目软件系统的成长记录
:::

::: warning 注意
**以版本号 5.0.0-SNAPSHOT 为例, 分别对应 X.Y.Z-LC**
:::

<table>
  <tr>
    <td>版本号</td>
	<td>描述名称</td>
	<td>描述说明</td>
  </tr>
  <tr>
    <td rowspan="4">X. Y. Z -LC</td>
	<td>生命周期 LC</td>
	<td>LC(life cycle)表示软件处于开发过程中的某一个阶段，取值范围如下: <br/><b>1、SNAPSHOT: 表示编码阶段</b><br/><b>2、BETA: 表示测试阶段</b><br/><b>3、RELEASE: 表示上线运行阶段</b></td>
  </tr>
  <tr>
    <td>修订号 Z</td>
	<td>用自然数表示软件的修订号, 初始值为 0</td>
  </tr>
  <tr>
    <td>次版本 Y </td>
	<td>用自然数表示软件的次版本号, 初始值为 0</td>
  </tr>
  <tr>
    <td>主版本 X </td>
	<td>用自然数表示软件的主版本号, 初始值为 1</td>
  </tr>
</table>