package com.orm.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * MyBatisPLus 自动填充（程序通用）基类
 *
 * <p style="color:red">
 *     在具体的业务实体类上, 可添加 {@link TableName#excludeProperty()} 属性进行本类属性名的排除（假如你继承了本类却又只使用了部分属性的话）
 * </p>
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
@SuppressWarnings("JavaDoc")
public class BaseEntity<T extends Model<T>> extends Model<T> {

    private static final long serialVersionUID = -4351991239120575147L;
    
    /**
     * 创建人 ID
     *
     * @ignore
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer creatorId;

    /**
     * 创建人
     *
     * @ignore
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建时间
     *
     * @ignore
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改人 ID
     *
     * @ignore
     */
    @TableField(fill = FieldFill.UPDATE)
    private Integer modifierId;

    /**
     * 修改人
     *
     * @ignore
     */
    @TableField(fill = FieldFill.UPDATE)
    private String modifier;

    /**
     * 修改时间
     *
     * @ignore
     */
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime modifyTime;

    /**
     * 当前机构节点 ID
     *
     * @ignore
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer orgId;

    /**
     * 数据权限范围
     *
     * @ignore
     */
    @TableField(fill = FieldFill.INSERT)
    private String scope;

    /**
     * 逻辑删除
     *
     * <ul>
     *     <li>0：未删除</li>
     *     <li>1：已删除</li>
     * </ul>
     *
     * @ignore
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer hasDel;

    /**
     * getter function
     *
     * @return {@link Integer}
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * setter function
     *
     * @param creatorId {@link #creatorId}
     * @return {@link BaseEntity}
     */
    public BaseEntity<T> setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link String}
     */
    public String getCreator() {
        return creator;
    }

    /**
     * setter function
     *
     * @param creator {@link #creator}
     * @return {@link BaseEntity}
     */
    public BaseEntity<T> setCreator(String creator) {
        this.creator = creator;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link LocalDateTime}
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * setter function
     *
     * @param createTime {@link #createTime}
     * @return {@link BaseEntity}
     */
    public BaseEntity<T> setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link Integer}
     */
    public Integer getModifierId() {
        return modifierId;
    }

    /**
     * setter function
     *
     * @param modifierId {@link #modifierId}
     * @return {@link BaseEntity}
     */
    public BaseEntity<T> setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link String}
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * setter function
     *
     * @param modifier {@link #modifier}
     * @return {@link BaseEntity}
     */
    public BaseEntity<T> setModifier(String modifier) {
        this.modifier = modifier;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link LocalDateTime}
     */
    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    /**
     * setter function
     *
     * @param modifyTime {@link #modifyTime}
     * @return {@link BaseEntity}
     */
    public BaseEntity<T> setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link Integer}
     */
    public Integer getOrgId() {
        return orgId;
    }

    /**
     * setter function
     *
     * @param orgId {@link #orgId}
     * @return {@link BaseEntity}
     */
    public BaseEntity<T> setOrgId(Integer orgId) {
        this.orgId = orgId;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link String}
     */
    public String getScope() {
        return scope;
    }

    /**
     * setter function
     *
     * @param scope {@link #scope}
     * @return {@link BaseEntity}
     */
    public BaseEntity<T> setScope(String scope) {
        this.scope = scope;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link Integer}
     */
    public Integer getHasDel() {
        return hasDel;
    }

    /**
     * setter function
     *
     * @param hasDel {@link #hasDel}
     * @return {@link BaseEntity}
     */
    public BaseEntity<T> setHasDel(Integer hasDel) {
        this.hasDel = hasDel;
        return this;
    }
}
