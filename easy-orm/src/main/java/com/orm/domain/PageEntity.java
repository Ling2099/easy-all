package com.orm.domain;

/**
 * 自定义系统分页对象
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
@SuppressWarnings("JavaDoc")
public class PageEntity extends PageScope {

    /**
     * 当前页码
     *
     * @required
     * @mock 1
     */
    private Integer pageNum;

    /**
     * 每页数据条数
     *
     * @required
     * @mock 10
     */
    private Integer pageSize;

    /**
     * getter function
     *
     * @return
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
     * @return {@link Integer}
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
}
