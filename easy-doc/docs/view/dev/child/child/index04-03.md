# Word、Excel 操作

---

::: warning 注意
由于将此 API 内容解耦至文件服务的话，会造成额外的网络 I/O 的开销，所以我写进了核心包中，如果压根就没使用到，可以排除，如下
:::

~~~Xml
<dependency>
    <groupId>com.hysoft</groupId>
    <artifactId>hy-common-core</artifactId>
    <exclusions>
        <exclusion>
            <groupId>com.hysoft</groupId>
            <artifactId>hy-common-file</artifactId>
        </exclusion>
    </exclusions>
</dependency>
~~~

::: warning 注意
模板文件直接上传至对象存储中，业务系统只需要统一管理文件地址即可
:::

## Word 模板导出

::: tip 提示
目前支持四种方式的模板导出
+ 纯表单
+ 表单 + 列表
+ 表单 + 图片
+ 表单 + 列表 + 图片
:::

+ 无返回值静态方法

~~~Java
/**
 * 导出 Word 文件
 *
 * @param wordParam Word 模板导出 参数对象
 * @param response  HTTP 响应
 */
FileUtils.exportWord(WordParam wordParam, HttpServletResponse response)
~~~

<table>
  <tr>
    <td>入参类名</td>
    <td>成员变量</td>
    <td>变量类型</td>
    <td>是否必传</td>
    <td>解释</td>
  </tr>
  <tr>
    <td rowspan="6">WordParam</td>
    <td>temp</td>
    <td>byte[]</td>
    <td>是</td>
    <td>模板文件字节数组</td>
  </tr>
  <tr>
    <td>fileName</td>
    <td>String</td>
    <td>是</td>
    <td>导出的文件名</td>
  </tr>
  <tr>
    <td>data</td>
    <td>Map&lt;String, Object&gt;</td>
    <td>是</td>
    <td>业务数据集合</td>
  </tr>
  <tr>
    <td>keys</td>
    <td>List&lt;String&gt;</td>
    <td>否</td>
    <td>待填充换数据的 Key</td>
  </tr>
  <tr>
    <td>image</td>
    <td>Map&lt;String, Args&gt;</td>
    <td>否</td>
    <td>图片数据集合</td>
  </tr>
  <tr>
    <td>wordEnum</td>
    <td>WordEnum</td>
    <td>是</td>
    <td>word 导出的形式</td>
  </tr>
</table>

<table>
  <tr>
    <td>静态内部类类名</td>
    <td>成员变量</td>
    <td>变量类型</td>
    <td>解释</td>
  </tr>
  <tr>
    <td rowspan="3">WordParam.Args</td>
    <td>image</td>
    <td>byte[]</td>
    <td>图片地址</td>
  </tr>
  <tr>
    <td>width</td>
    <td>Integer</td>
    <td>图片的宽度</td>
  </tr>
  <tr>
    <td>high</td>
    <td>Integer</td>
    <td>图片的高度</td>
  </tr>
</table>

<table>
  <tr>
    <td>内部枚举类类名</td>
    <td>成员变量</td>
    <td>解释</td>
  </tr>
  <tr>
    <td rowspan="4">WordParam.WordEnum</td>
    <td>FORM</td>
    <td>纯表单</td>
  </tr>
  <tr>
    <td>FORM_TABLE</td>
    <td>表单 + 列表</td>
  </tr>
  <tr>
    <td>FORM_IMAGE</td>
    <td>表单 + 图片</td>
  </tr>
  <tr>
    <td>FORM_TABLE_IMAGE</td>
    <td>表单 + 列表 + 图片</td>
  </tr>
</table>

+ 导出纯表单的 word 文件至客户端

![avatar](/assets/img/hy-word1.png)

~~~Java
/**
 * <p>
 * 系统基础服务 -- 用户表 前端控制器
 * </p>
 *
 * @author LZH
 * @since 2022-02-20
 */
@RestController
@RequestMapping("/sys-user")
@Api(tags = "系统基础服务 -- 用户管理")
public class SysUserController {

    @Autowired
    private SysUserService service;

    /**
     * 导出 word 文件至客户端
     *
     * @param response 客户端响应
     */
    @ApiOperation(
            value = "导出 word 文件至客户端",
            httpMethod = "GET"
    )
    @GetMapping("exportWord")
    public void exportWord(HttpServletResponse response) {
        service.exportWord(response);
    }
}
~~~

~~~Java
/**
 * <p>
 * 系统基础服务 -- 用户表 服务类
 * </p>
 *
 * @author LZH
 * @since 2022-02-20
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 导出 word 文件至客户端
     *
     * @param response 客户端响应
     */
    void exportWord(HttpServletResponse response);
}
~~~

~~~Java
/**
 * <p>
 * 系统基础服务 -- 用户表 服务实现类
 * </p>
 *
 * @author LZH
 * @since 2022-02-20
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private RemoteFileService fileService;

    /**
     * 导出 word 文件至客户端
     *
     * @param response 客户端响应
     */
    @Override
    public void exportWord(HttpServletResponse response) {
        // 定义导出文件名称（真实环境下不应该写死）
        String fileName = "导出文件.docx";
        // 根据条件查询数据, Mybatis Plus 自动转换为 Map 集合
        final Map<String, Object> map = super.getMap(new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, 1));
        // 远程调用文件服务接口, 获取模板的字节数组
        final byte[] bytes = fileService.getBytes("/temp/测试文件.docx").getData();
        // 调用公共文件静态接口, 导出文件
        FileUtils.exportWord(
            new WordParam(bytes, fileName, map, WordParam.WordEnum.FORM),
            response
        );
    }
}
~~~

+ 导出表单 + 列表的 word 文件至客户端

![avatar](/assets/img/hy-word2.png)

~~~Java
/**
 * 导出 word 文件至客户端
 *
 * <p>其它代码都一致, 就不贴出来了</p>
 *
 * @param response 客户端响应
 */
@Override
public void exportWord(HttpServletResponse response) {
    // 定义导出文件名称（真实环境下不应该写死）
    String fileName = "导出文件.docx";
    // 根据条件查询数据, Mybatis Plus 自动转换为 Map 集合
    final Map<String, Object> map = super.getMap(new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, 1));

    // Mybatis Plus 列表查询
    final List<SysUser> list = super.list();
    // 注意: 这里一定要将 list 集合 put 进 Map 中, 并且其 Key 与后续入参参数的值一致
    map.put("user", list);

    // 远程调用文件服务接口, 获取模板的字节数组
    final byte[] bytes = fileService.getBytes("/temp/测试文件.docx").getData();
    // 调用公共文件静态接口, 导出文件
    FileUtils.exportWord(
        new WordParam()
            .setTemp(bytes)
            .setData(map)
            .setFileName(fileName)
            // 这里的字符可以有多个（例如你要写多个列表至文件）, 但必须与 Map 集合中的 Key 一致
            .setKeys(new ArrayList<String>(){{add("user");}})
            .setWordEnum(WordParam.WordEnum.FORM_TABLE),
        response
    );
}
~~~

+ 导出表单 + 图片的 word 文件至客户端

![avatar](/assets/img/hy-word3.png)

~~~Java
/**
 * 导出 word 文件至客户端
 *
 * <p>其它代码都一致, 就不贴出来了</p>
 *
 * @param response 客户端响应
 */
@Override
public void exportWord(HttpServletResponse response) {
    // 定义导出文件名称（真实环境下不应该写死）
    String fileName = "导出文件.docx";
    // 根据条件查询数据, Mybatis Plus 自动转换为 Map 集合
    final Map<String, Object> map = super.getMap(new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, 1));

    // 定义远程调用的入参参数
    Map<String, String> param = new HashMap<>(8);
    param.put("temp", "/temp/测试文件.docx");
    param.put("img1", "/temp/img1.jpeg");
    param.put("img2", "/temp/img2.jpeg");
    // 调用远程文件服务接口, 获取模板文件的字节数组集合
    final Map<String, byte[]> data = fileService.getMapBytes(param).getData();

    // 定义关于图片的入参对象; WordParam.Args 构造函数还能对图片的宽、 高进行初始化
    Map<String, WordParam.Args> img = new HashMap<>(data.size());
    data.forEach((k, v) -> img.put(k, new WordParam.Args(v)));

    // 调用公共文件静态接口, 导出文件
    FileUtils.exportWord(
        new WordParam()
            .setTemp(data.get("temp"))
            .setData(map)
            .setImage(img)
            .setFileName(fileName)
            .setWordEnum(WordParam.WordEnum.FORM_IMAGE
        ),
        response
    );
}
~~~

+ 导出表单 + 列表 + 图片的 word 文件至客户端

![avatar](/assets/img/hy-word4.png)

~~~Java
/**
 * 导出 word 文件至客户端
 *
 * <p>其它代码都一致, 就不贴出来了</p>
 *
 * @param response 客户端响应
 */
@Override
public void exportWord(HttpServletResponse response) {
    // 定义导出文件名称（真实环境下不应该写死）
    String fileName = "导出文件.docx";
    // 根据条件查询数据, Mybatis Plus 自动转换为 Map 集合
    final Map<String, Object> map = super.getMap(new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, 1));

    // 注意: 这里一定要将 list 集合 put 进 Map 中, 并且其 Key 与后续入参参数的值一致
    map.put("user", super.list());

    // 定义远程调用的入参参数
    Map<String, String> param = new HashMap<>(8);
    param.put("temp", "/temp/测试文件.docx");
    param.put("img1", "/temp/img1.jpeg");
    param.put("img2", "/temp/img2.jpeg");
    // 调用远程文件服务接口, 获取模板文件的字节数组集合
    final Map<String, byte[]> data = fileService.getMapBytes(param).getData();

    // 定义关于图片的入参对象; WordParam.Args 构造函数还能对图片的宽、 高进行初始化
    Map<String, WordParam.Args> img = new HashMap<>(data.size());
    data.forEach((k, v) -> img.put(k, new WordParam.Args(v)));

    // 调用公共文件静态接口, 导出文件
    FileUtils.exportWord(
        new WordParam()
            .setTemp(data.get("temp"))
            .setData(map)
            .setImage(img)
            .setFileName(fileName)
            // 这里的字符可以有多个（例如你要写多个列表至文件）, 但必须与 Map 集合中的 Key 一致
            .setKeys(new ArrayList<String>(){{add("user");}})
            .setWordEnum(WordParam.WordEnum.FORM_TABLE_IMAGE
        ),
        response
    );
}
~~~

## Excel 模板导出

## Excel 导入解析