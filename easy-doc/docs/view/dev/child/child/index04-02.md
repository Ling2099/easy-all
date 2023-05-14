# 获取客户端 HTTP 信息

---

::: tip 提示
该工具类继承至 cn.hutool.http.HttpUtil，所以你同样可以使用 Hutool 的工具方法
:::

<table>
  <tr>
    <td>类名</td>
    <td>方法名</td>
    <td>是否静态</td>
    <td>入参参数</td>
    <td>返回类型</td>
    <td>使用说明</td>
  </tr>
  <tr>
    <td rowspan="3">HttpUtils</td>
    <td>getAddress</td>
    <td>是</td>
    <td>无</td>
    <td>String</td>
    <td>获取客户端 IP 地址</td>
  </tr>
  <tr>
    <td>getRequest</td>
    <td>是</td>
    <td>无</td>
    <td>HttpServletRequest</td>
    <td>获取 Request 请求体</td>
  </tr>
  <tr>
    <td>getHeader</td>
    <td>是</td>
    <td>消息头键名</td>
    <td>String</td>
    <td>获取消息头内容</td>
  </tr>
</table>