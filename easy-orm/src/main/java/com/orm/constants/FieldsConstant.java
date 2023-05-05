package com.orm.constants;

/**
 * 实体类 {@link com.orm.domain.BaseEntity} 成员变量名字符常量
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
public final class FieldsConstant {

    /**
     * 用户 ID
     */
    public static final String USER_ID = "userId";

    /**
     * 用户名
     */
    public static final String USER_NAME = "userName";

    /**
     * 机构 ID
     */
    public static final String ORG_ID = "orgId";

    /**
     * 机构 ID 集合
     * <P>类型为字符串, 如: 1, 2, 3</P>
     */
    public static final String ORG_IDS = "orgIds";

    /**
     * 数据权限范围
     *
     * <p>如: 000100020005</p>
     */
    public static final String SCOPE = "scope";

    /**
     * 数据权限标识
     *
     * <ol>
     *     <li>全部数据权限</li>
     *     <li>本级及以下数据权限</li>
     *     <li>本级数据权限</li>
     *     <li>仅本人数据权限</li>
     *     <li>自定义数据权限</li>
     * </ol>
     */
    public static final String SCOPE_TYPE = "scopeType";

    /**
     * 是否删除
     */
    public static final String HAS_DEL = "hasDel";

    /**
     * 创建人 ID
     */
    public static final String CREATOR_ID = "creatorId";

    /**
     * 创建人
     */
    public static final String CREATOR = "creator";

    /**
     * 创建时间
     */
    public static final String CREATE_TIME = "createTime";

    /**
     * 修改人 ID
     */
    public static final String MODIFIER_ID = "modifierId";

    /**
     * 修改人
     */
    public static final String MODIFIER = "modifier";

    /**
     * 修改时间
     */
    public static final String MODIFY_TIME = "modifyTime";

}
