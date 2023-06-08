package com.orm.domain;

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
@Deprecated
@SuppressWarnings("JavaDoc")
public class PageScope implements Serializable {

    private static final long serialVersionUID = 7532628568017037197L;

    /**
     * 数据权限的条件 SQL 片段
     *
     * <p>用于在分页查询或其它场景下管理数据权限</p>
     *
     * @ignore
     */
    private String chip;

    /**
     * getter function
     *
     * @return {@link String}
     */
    public String getChip() {
        return chip;
    }

    /**
     * setter function
     *
     * @param chip {@link #chip}
     */
    public void setChip(String chip) {
        this.chip = chip;
    }
}
