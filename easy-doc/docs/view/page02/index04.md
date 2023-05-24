### 文件相关

Maven:
~~~xml
<dependency>
    <groupId>com.github.Ling2099</groupId>
    <artifactId>easy-file</artifactId>
    <version>latest version</version>
</dependency>
~~~

Gradle:
~~~gradle
implementation 'com.github.Ling2099:easy-file:latest version'
// 或者
implementation group: 'com.github.Ling2099', name: 'easy-file', version: 'latest version'
~~~

#### Excel 文件


::: tip 导出
以下例子的数据来源如下, 后续不再重复
:::

~~~java
/**
 * 梁山好汉 109 犟
 */
@Data
@AllArgsConstructor
public class User {

    @ExcelProperty(value = "座次", index = 0)
    private Integer id;

    @ExcelProperty(value = "姓名", index = 1)
    private String name;

    @ExcelProperty(value = "诨名", index = 2)
    private String nickName;

    @ExcelProperty(value = "星宿", index = 3)
    private String constellation;

}
~~~

~~~java
public List<User> getList() {
    return Arrays.asList(
        new User(1, "宋江", "呼保义", "天魁星"),
        new User(2, "卢俊义", "玉麒麟", "天罡星"),
        new User(3, "吴用", "智多星", "天机星"),
        new User(4, "公孙胜", "入云龙", "天闲星"),
        new User(5, "关胜", "大刀", "天勇星"),
        new User(6, "林冲", "豹子头", "天雄星"),
        new User(7, "秦明", "霹雳火", "天猛星"),
        new User(109, "三天不打, 就这么犟", "敖卵犟", "行星+恒星")
    );
}
~~~

&#x1F410; 由数据集合导出 Excel 文件

~~~java
@GetMapping
public void exportFile(HttpServletResponse response) {
    List<User> list = getList();
    ExcelTool.export("梁山好汉 109 犟.xlsx", "座次排定", list, response);
}
~~~

&#x1F40B; 由自定义模板和数据集合导出 Excel 文件

~~~java
// 待更新
~~~


::: tip 解析
以下例子的数据来源如下, 后续不再重复
:::