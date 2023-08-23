package com.orm.domain;

import java.io.Serializable;

/**
 * 自定义系统分页对象
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
@SuppressWarnings({"JavaDoc", "SpellCheckingInspection"})
public class PageEntity implements Serializable {

    private static final long serialVersionUID = -7066207669309293709L;

    /**
     * 当前页码
     *
     * @required
     * @mock 1
     * @since 1.0.0
     */
    private Integer pageNum;

    /**
     * 每页数据条数
     *
     * @required
     * @mock 10
     * @since 1.0.0
     */
    private Integer pageSize;

    /**
     * 排序字段（正序）
     *
     * @mock {"id", "name", "sex"}
     * @since 1.0.11
     */
    private String[] ascs;

    /**
     * 排序字段（倒序）
     *
     * @mock {"id", "name", "sex"}
     * @since 1.0.11
     */
    private String[] descs;

    /**
     * getter function
     *
     * @return {@link #pageNum}
     */
    public Integer getPageNum() {
        return pageNum;
    }

    /**
     * setter function
     *
     * @param pageNum {@link #pageNum}
     * @return {@link PageEntity}
     */
    public PageEntity setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link #pageSize}
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * setter function
     *
     * @param pageSize {@link #pageSize}
     * @return {@link PageEntity}
     */
    public PageEntity setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link #ascs}
     * @since 1.0.11
     */
    public String[] getAscs() {
        return ascs;
    }

    /**
     * setter function
     *
     * @param ascs {@link #ascs}
     * @return {@link PageEntity}
     * @since 1.0.11
     */
    public PageEntity setAscs(String[] ascs) {
        this.ascs = ascs;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link #descs}
     * @since 1.0.11
     */
    public String[] getDescs() {
        return descs;
    }

    /**
     * setter function
     *
     * @param descs {@link #descs}
     * @since 1.0.11
     */
    public void setDescs(String[] descs) {
        this.descs = descs;
    }

    /**
     * 验证 {@link #ascs} 数组不为空
     *
     * @return boolean
     * @since 1.0.11
     */
    public boolean isNotBlankAscs() {
        return this.ascs != null && this.ascs.length != 0;
    }

    /**
     * 验证 {@link #descs} 数组不为空
     *
     * @return boolean
     * @since 1.0.11
     */
    public boolean isNotBlankDescs() {
        return this.descs != null && this.descs.length != 0;
    }
}
