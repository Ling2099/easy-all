package com.orm.tool;

import com.orm.constants.FieldsConstant;

/**
 * 本地线程副本中的数据工具类
 *
 * <p>可继承并实现额外的工具方法</p>
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-05
 */
public class StatementTool {

    /**
     * 获取当前用户主键 ID
     *
     * @return {@link Integer}
     */
    public static Integer getUserId() {
        return ThreadLocalTool.get(FieldsConstant.USER_ID);
    }

    /**
     * 获取当前用户名
     *
     * @return {@link String}
     */
    public static String getUserName() {
        return ThreadLocalTool.get(FieldsConstant.USER_NAME);
    }

    /**
     * 获取当前用户的机构 ID
     *
     * @return {@link Integer}
     */
    public static Integer getOrgId() {
        return ThreadLocalTool.get(FieldsConstant.ORG_ID);
    }

    /**
     * 获取当前用户所拥有的机构 ID 集合字符
     *
     * <p>如: 1, 2, 3</p>
     *
     * @return {@link String}
     */
    public static String getOrgIds() {
        return ThreadLocalTool.get(FieldsConstant.ORG_IDS);
    }

    /**
     * 获取当前用户的数据权限
     *
     * <p>如: 000100020005</p>
     *
     * @return {@link String}
     */
    public static String getScope() {
        return ThreadLocalTool.get(FieldsConstant.SCOPE);
    }

    /**
     * 获取当前用户的数据权限类型
     *
     * <ol>
     *     <li>全部数据权限（无 SQL 条件）</li>
     *     <li>本级及以下数据权限 （{@code scope LIKE '?%'}）</li>
     *     <li>本级数据权限 （{@code scope = ?}）</li>
     *     <li>仅本人数据权限 （{@code userId = ?}）</li>
     *     <li>自定义数据权限 （{@code orgId IN (?, ?, ?)}）</li>
     * </ol>
     *
     * @return {@link Integer}
     */
    public static Integer getScopeType() {
        return ThreadLocalTool.get(FieldsConstant.SCOPE_TYPE);
    }

}
