package com.orm.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * MyBatisPLus 自动填充（程序通用）基类
 *
 * <p style="color:red">
 *     可通过 {@link TableName#excludeProperty()} 属性进行本类域的排除
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
     * 创建时间
     *
     * @ignore
     * @mock 2099-12-31 23:59:59
     */
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     *
     * @ignore
     * @mock 2099-12-31 23:59:59
     */
    @JsonIgnore
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     *
     * <ul>
     *     <li>0：未删除</li>
     *     <li>1：已删除</li>
     * </ul>
     *
     * @ignore
     * @mock 0
     */
    @JsonIgnore
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer del;

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
     * @return {@link LocalDateTime}
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * setter function
     *
     * @param updateTime {@link #updateTime}
     * @return {@link BaseEntity}
     */
    public BaseEntity<T> setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link Integer}
     */
    public Integer getDel() {
        return del;
    }

    /**
     * setter function
     *
     * @param del {@link #del}
     * @return {@link BaseEntity}
     */
    public BaseEntity<T> setDel(Integer del) {
        this.del = del;
        return this;
    }
}
