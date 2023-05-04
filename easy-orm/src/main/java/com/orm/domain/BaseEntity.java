package com.orm.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Data
@Accessors(chain = true)
@SuppressWarnings({"Lombok", "JavaDoc"})
public class BaseEntity<T extends Model<T>> extends Model<T> {

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

}
