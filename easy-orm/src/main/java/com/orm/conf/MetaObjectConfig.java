package com.orm.conf;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充配置类
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023/05/06
 */
@ConditionalOnProperty(name = "easy.meta")
public class MetaObjectConfig implements MetaObjectHandler {

    /**
     * 实体类公共域
     *
     * <ul>
     *     <li>是否删除</li>
     *     <li>创建时间</li>
     *     <li>修改时间</li>
     * </ul>
     */
    private static final String DEL = "del", CREATE_TIME = "createTime", UPDATE_TIME = "updateTime";

    /**
     * 当数据新增时所执行的自动填充逻辑
     *
     * @param meta {@link MetaObject}
     */
    @Override
    public void insertFill(MetaObject meta) {
        if (meta.hasSetter(DEL)) {
            this.fillStrategy(meta, DEL, 0);
        }

        if (meta.hasSetter(CREATE_TIME)) {
            this.fillStrategy(meta, CREATE_TIME, LocalDateTime.now());
        }
    }

    /**
     * 当数据修改时所执行的自动填充逻辑
     *
     * @param meta {@link MetaObject}
     */
    @Override
    public void updateFill(MetaObject meta) {
        if (meta.hasSetter(UPDATE_TIME)) {
            this.fillStrategy(meta, UPDATE_TIME, LocalDateTime.now());
        }
    }
}
