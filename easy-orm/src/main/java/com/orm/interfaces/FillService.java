package com.orm.interfaces;

import org.apache.ibatis.reflection.MetaObject;

/**
 * 自定义扩展的 MyBatis-Plus 填充接口
 *
 * @see com.orm.conf.MetaObjectConfig
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-14
 */
public interface FillService {

    /**
     * 自定义新增时的填充方法
     *
     * @param meta {@link MetaObject}
     */
    void insertFill(MetaObject meta);

    /**
     * 自定义修改时的填充方法
     *
     * @param meta {@link MetaObject}
     */
    void updateFill(MetaObject meta);

}
