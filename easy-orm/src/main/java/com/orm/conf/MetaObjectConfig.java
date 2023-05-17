package com.orm.conf;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.orm.constants.FieldsConstant;
import com.orm.tool.StatementTool;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充配置类
 *
 * @author LZH
 * @version 5.0.0
 * @since 2023-05-06
 */
public class MetaObjectConfig implements MetaObjectHandler {

    /**
     * 当数据新增时所执行的自动填充逻辑
     *
     * @param meta {@link MetaObject}
     */
    @Override
    public void insertFill(MetaObject meta) {
        String[] names = meta.getGetterNames();
        for (String name : names) {
            switch (name) {
                case FieldsConstant.ORG_ID     : this.fillStrategy(meta, name, 1); break;
                case FieldsConstant.SCOPE      : this.fillStrategy(meta, name, StatementTool.getScope()); break;
                case FieldsConstant.HAS_DEL    : this.fillStrategy(meta, name, 0); break;
                case FieldsConstant.CREATOR_ID : this.fillStrategy(meta, name, StatementTool.getUserId()); break;
                case FieldsConstant.CREATOR    : this.fillStrategy(meta, name, StatementTool.getUserName()); break;
                case FieldsConstant.CREATE_TIME: this.fillStrategy(meta, name, LocalDateTime.now()); break;
                default: {}
            }
        }
    }

    /**
     * 当数据修改时所执行的自动填充逻辑
     *
     * @param meta {@link MetaObject}
     */
    @Override
    public void updateFill(MetaObject meta) {
        String[] names = meta.getGetterNames();
        for (String name : names) {
            switch (name) {
                case FieldsConstant.MODIFIER_ID: this.fillStrategy(meta, name, StatementTool.getUserId()); break;
                case FieldsConstant.MODIFIER   : this.fillStrategy(meta, name, StatementTool.getUserName()); break;
                case FieldsConstant.MODIFY_TIME: this.fillStrategy(meta, name, LocalDateTime.now()); break;
                default: {}
            }
        }
    }
}
