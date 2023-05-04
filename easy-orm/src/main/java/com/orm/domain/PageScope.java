package com.orm.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 数据权限
 *
 * <p>SQL 条件基类</p>
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
@Data
@SuppressWarnings("JavaDoc")
public class PageScope implements Serializable {

    /**
     * 数据权限的条件 SQL 片段
     *
     * <p>用于在分页查询或其它场景下管理数据权限</p>
     *
     * @ignore
     */
    private String chip;

}
