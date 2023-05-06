package com.test.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serial;

/**
 * TODO
 *
 * @author LZH
 * @version 5.0.0
 * @since 2023-05-06
 */
@TableName(value = "user")
public class User extends Model<User> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID
     *
     * @mock 1
     * @since 5.0.0
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     *
     * @mock 张三
     * @since 5.0.0
     */
    private String name;

    /**
     * 数据权限范围
     *
     * @ignore
     */
    @TableField(fill = FieldFill.INSERT)
    private String scope;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
