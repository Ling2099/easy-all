# 开发规范

::: tip 提示
+ 点击下载 [阿里巴巴嵩山版java开发规范](http://192.168.0.35:9000/hy-cloud/阿里巴巴嵩山版java开发规范.zip), 仔细阅读, 你收获的不仅仅是规范
+ 阿里的崇山版已经对开发时各个细节描述很详细, 没必要重复造轮子, 只是对于我们自己的业务有几点需要注意的地方
:::

::: danger 重要
**所有 Java 代码中, 类、方法、属性（成员变量）必须写上文档注释, 且遵循 [JavaDoc](http://c.biancheng.net/view/6262.html) 规范, 在业务场景复杂时, 更应该标注好关键代码的含义**, 换把椅子坐, 开发 JDK 或其他框架的大佬们, 注释写得比你代码还多, 你有什么理由说: "就这几行代码, 还需要写注释吗?"
:::

+ 这是我编码过程中关于业务的代码; 我始终相信, 注释不仅帮助别人理解, 更能帮助自己长时间后的回忆

~~~Java
    /**
     * 流程启动（送审）时的范围检查
     *
     * 注意: 流程的建立是各公司（集团）的负责人/管理员, 违背了这一原则就凉凉
     *
     * <p>
     *     如何来确定某一条流程是该机构的管理员创建的呢 ?
     *     根据当前用户的部门、公司、集团 ID 去匹配管理员的部门、公司、集团 ID,
     *     且遵循 '最短' 上下级关系原则, 当满足条件时则不往后寻找,
     *     若都不满足时, 再查看是否拥有 '全机构范围'
     * </p>
     *
     * <p>
     *     适用范围优先级:
     *     1、当前机构范围
     *     2、子机构范围
     *     3、同级机构范围
     *     4、全机构范围
     * </p>
     *
     * <p>
     *     例如: 某机构下的 admin 给其建立了 4 种类型的同业务流程,
     *     某个用户点击了 '送审' 按钮后, 他应该适用哪一条流程呢?
     *     1、首先校验当前机构范围, 判断条件是 用户.scope == admin.scope
     *     2、如果不满足, 再校验子机构范围, 判断条件是 用户.scope like 'admin.scope%'
     *     3、依然不满足的情况下, 校验同级别机构范围, 判断条件是 用户.scope.length == admin.length
     *     4、还不满足时, 校验是否有全机构范围
     *     最后, 如果以上都不满足, 让他找管理员吧
     * </p>
     *
     * <P>
     *     值得注意的是, 在业务上有个 bug, 当这个管理员建立了两个相同类型、相同菜单的流程时,
     *     我们只取已获得的 {@code flow_menu} 表集合中 id 最大那条（最新）
     * </P>
     *
     * @param menuId 菜单 ID
     * @return FlowRunTimeVo
     */
    @Override
    public FlowRunTimeVo check(Integer menuId) {
        // 缓存中读取已勾选的全部流程菜单, 用作后面对比
        List<FlowMenu> list = redisService.getCacheObject(FlowCacheConstants.KEY_FLOW_MENU);

        // 当前送审人的机构数据权限字符
        final String currentScope = ServletUtils.getScope();

        // 当前送审人的国资委、集团、公司、部门 ID
        final Integer sasacId = ServletUtils.getSasacId(),
                      groupId = ServletUtils.getGroupId(),
                      companyId = ServletUtils.getCompanyId(),
                      deptId = ServletUtils.getDeptId();

        // 0、如果缓存里木有, 则查询数据库
        // 1、创建 ReferencePipeline.Head 对象
        // 2、首先过滤掉已停用/已删除的菜单流程
        // 3、然后过滤当前菜单的流程
        // 4、其次过滤是否为该机构的管理员创建
        // 即送审人的机构节点 ID 是否等于创建这个流程的管理员的部门、公司、集团、国资委 ID
        // 5、根据流程的范围类型正序
        // 6、过滤流程的适用范围
        // 7、根据 ID 倒序
        // 8、获取最终的流程范围对象
        // 9、没有的话抛出异常
        final FlowMenu flowMenu = Optional.ofNullable(list)
                .orElse(flowMenuService.list())
                .stream()
                .filter(item -> item.getHasDisable().equals(0) && item.getDel().equals(0))
                .filter(item -> item.getMenuId().equals(menuId))
                .filter(item -> {
                    final Integer orgId = item.getOrgId();
                    return orgId.equals(sasacId) || orgId.equals(groupId) || orgId.equals(companyId) || orgId.equals(deptId);
                })
                .sorted(Comparator.comparing(FlowMenu::getFlowScope))
                .filter(item -> {
                    final String scope = item.getScope();
                    switch (item.getFlowScope()) {
                        case 1: return currentScope.equals(scope);
                        case 2: return currentScope.contains(scope);
                        case 3: return currentScope.length() == scope.length();
                        case 4: return true;
                        default: return false;
                    }
                })
                .max(Comparator.comparing(FlowMenu::getId))
                .orElseThrow(() -> new BusinessException("无权限操作此流程服务, 请联系管理员"));

        // 返回 VO 对象
        return new FlowRunTimeVo(flowMenu.getDeployId(), flowMenu.getFlowKey(), flowMenu.getFlowName());
    }
~~~

::: danger 警告
**库命名规则** 全部以 **hy-** 开头且**小写**, 后面跟上能准确表达此服务的简短单词, 如下图所示
:::

![avatar](/assets/img/hy-db.png)

::: danger 警告
**表命名规则** 每个库中的表全部以该库的大致含义加下划线开头且**小写**, 后面跟上能表达该表业务作用的单词, 多个单词使用下划线隔开, 如下图所示的表全为 hy-system 数据库中
:::

![avatar](/assets/img/hy-table.png)

::: danger 警告
**表结构必须要有注释, 阐明该表的作用以及所涉及到的服务名或用途; 表中的每个字段也必须要有注释, 阐明该字段的具体含义; 另外索引也必须要写注释.**
:::

![avatar](/assets/img/hy-table1.png)

![avatar](/assets/img/hy-table2.png)

![avatar](/assets/img/hy-table3.png)

::: warning 重要
+ **数据库表结构**（业务表或主表强制添加如下字段）, 也可以直接复制 192.168.0.120 上 hy-test 库表中的结构, **注意: 各字段名均小写, 每个单词之间用下划线隔开**
+ **强制索引**中显示**否**的字段, 并不是标准答案, 应视具体场景而定是否需要建立索引
:::

::: warning 重要
另外提一嘴，合理的表结构不但节约数据库表空间、节约索引存储，更重要的是提升检索速度；
我们应该在设计时就考虑好如何节约空间，而不是管他三七二十一，先把功能写出来了再说（虽然我也知道这是个市场化竞争激烈的年代），**或许你也玩过32个关卡但只占用64KB存储空间的超级马里奥**
:::

::: tip 无奈
我也不想搞这么多公共字段, 纯属业务的锅
:::

<table>
  <tr>
    <td>字段名</td>
	<td>类型</td>
	<td>长度</td>
	<td>是否主键</td>
	<td>其余类型</td>
	<td>强制索引</td>
	<td>注释</td>
  </tr>
  <tr>
    <td>id</td>
	<td>int</td>
	<td>8</td>
	<td>是</td>
	<td>不是null、不勾选自动递增、无符号</td>
	<td>引擎内置</td>
	<td>主键ID</td>
  </tr>
  <tr>
    <td>sasac_id</td>
    <td>smallint</td>
    <td>4</td>
    <td>否</td>
    <td>无符号</td>
    <td>否</td>
    <td>国资委ID</td>
  </tr>
  <tr>
    <td>sasac_name</td>
    <td>varchar</td>
    <td>32</td>
    <td>否</td>
    <td>无</td>
    <td>否</td>
    <td>国资委名称</td>
  </tr>
  <tr>
    <td>group_id</td>
    <td>smallint</td>
    <td>4</td>
    <td>否</td>
    <td>无符号</td>
    <td>否</td>
    <td>集团ID</td>
  </tr>
  <tr>
    <td>group_name</td>
    <td>varchar</td>
    <td>32</td>
    <td>否</td>
    <td>无</td>
    <td>否</td>
    <td>集团名称</td>
  </tr>
  <tr>
    <td>company_id</td>
    <td>smallint</td>
    <td>4</td>
    <td>否</td>
    <td>无符号</td>
    <td>否</td>
    <td>公司ID</td>
  </tr>
  <tr>
    <td>company_name</td>
    <td>varchar</td>
    <td>32</td>
    <td>否</td>
    <td>无</td>
    <td>否</td>
    <td>公司名称</td>
  </tr>
  <tr>
    <td>dept_id</td>
    <td>smallint</td>
    <td>4</td>
    <td>否</td>
    <td>无符号</td>
    <td>否</td>
    <td>部门ID</td>
  </tr>
  <tr>
    <td>dept_name</td>
    <td>varchar</td>
    <td>32</td>
    <td>否</td>
    <td>无</td>
    <td>否</td>
    <td>部门名称</td>
  </tr>
  <tr>
    <td>org_id</td>
	<td>smallint</td>
	<td>4</td>
	<td>否</td>
	<td>无符号</td>
	<td>否</td>
	<td>机构节点ID</td>
  </tr>
  <tr>
    <td>org_name</td>
	<td>varchar</td>
	<td>64</td>
	<td>否</td>
	<td>无</td>
	<td>否</td>
	<td>机构名称</td>
  </tr>
  <tr>
    <td>credit_code</td>
	<td>char</td>
	<td>18</td>
	<td>否</td>
	<td>无</td>
	<td>否</td>
	<td>社会信用代码</td>
  </tr>
  <tr>
    <td>creator_id</td>
	<td>smallint</td>
	<td>4</td>
	<td>否</td>
	<td>无符号</td>
	<td>否</td>
	<td>创建人ID</td>
  </tr>
  <tr>
    <td>creator</td>
	<td>varchar</td>
	<td>32</td>
	<td>否</td>
	<td>无</td>
	<td>否</td>
	<td>创建人</td>
  </tr>
  <tr>
    <td>create_time</td>
	<td>datetime</td>
	<td>无</td>
	<td>否</td>
	<td>无</td>
	<td>否</td>
	<td>创建时间</td>
  </tr>
  <tr>
    <td>modifier_id</td>
	<td>smallint</td>
	<td>4</td>
	<td>否</td>
	<td>无符号</td>
	<td>否</td>
	<td>修改人ID</td>
  </tr>
  <tr>
    <td>modifier</td>
	<td>varchar</td>
	<td>32</td>
	<td>否</td>
	<td>无</td>
	<td>否</td>
	<td>修改人</td>
  </tr>
  <tr>
    <td>modify_time</td>
	<td>datetime</td>
	<td>无</td>
	<td>否</td>
	<td>无</td>
	<td>否</td>
	<td>修改时间</td>
  </tr>
  <tr>
    <td>scope</td>
	<td>varchar</td>
	<td>32</td>
	<td>否</td>
	<td>无</td>
	<td>是</td>
	<td>数据权限范围</td>
  </tr>
  <tr>
    <td>del</td>
	<td>tinyint</td>
	<td>2</td>
	<td>否</td>
	<td>默认: 0</td>
	<td>否</td>
	<td>逻辑删除; 0：未删除 1：已删除</td>
  </tr>
</table>

::: tip SQL 执行语句
当然, 你也可以在表创建完后, 直接执行如下语句, 以便快速的添加公共字段
:::

~~~SQL
ALTER TABLE 你的表名
ADD COLUMN `id` int(8) UNSIGNED NOT NULL PRIMARY KEY COMMENT '主键ID',
ADD COLUMN `sasac_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '国资委ID',
ADD COLUMN `sasac_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '国资委名称',
ADD COLUMN `group_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '集团ID',
ADD COLUMN `group_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '集团名称',
ADD COLUMN `company_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '公司ID',
ADD COLUMN `company_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公司名称',
ADD COLUMN `dept_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '部门ID',
ADD COLUMN `dept_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门名称',
ADD COLUMN `org_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '机构节点ID',
ADD COLUMN `credit_code` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '社会信用代码',
ADD COLUMN `creator_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '创建人ID',
ADD COLUMN `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
ADD COLUMN `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
ADD COLUMN `modifier_id` smallint(4) UNSIGNED NULL DEFAULT NULL COMMENT '修改人ID',
ADD COLUMN `modifier` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
ADD COLUMN `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
ADD COLUMN `scope` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据权限范围',
ADD COLUMN `del` tinyint(2) NULL DEFAULT 0 COMMENT '逻辑删除; 0：未删除 1：已删除',
ADD INDEX `idx_scope`(`scope`) USING BTREE COMMENT '数据权限'
~~~