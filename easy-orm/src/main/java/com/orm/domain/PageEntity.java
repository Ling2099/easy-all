package com.orm.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 自定义系统分页对象
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
@Data
@Accessors(chain = true)
@SuppressWarnings({"JavaDoc", "Lombok"})
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

}
