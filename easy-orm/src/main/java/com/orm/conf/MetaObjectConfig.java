package com.orm.conf;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.orm.constants.FieldsConstant;
import com.orm.interfaces.FillService;
import com.orm.tool.StatementTool;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充配置类
 *
 * <P>通过配置项参数 {@code easy.fill} 关闭/开启该配置类的加载, 同时支持自定义扩展 {@link FillService}</P>
 *
 * @author LZH
 * @version 5.0.0
 * @since 2023-05-06
 */
@ConditionalOnProperty(name = "easy.fill")
public class MetaObjectConfig implements MetaObjectHandler {

    private final FillService service;

    public MetaObjectConfig(FillService service) {
        this.service = service;
    }

    /**
     * 当数据新增时所执行的自动填充逻辑
     *
     * @param meta {@link MetaObject}
     */
    @Override
    public void insertFill(MetaObject meta) {
        if (service != null) {
            service.insertFill(meta);
        }

        String[] names = meta.getGetterNames();
        for (String name : names) {
            switch (name) {
                case FieldsConstant.ORG_ID       -> this.fillStrategy(meta, name, 1);
                case FieldsConstant.SCOPE        -> this.fillStrategy(meta, name, StatementTool.getScope());
                case FieldsConstant.HAS_DEL      -> this.fillStrategy(meta, name, 0);
                case FieldsConstant.CREATOR_ID   -> this.fillStrategy(meta, name, StatementTool.getUserId());
                case FieldsConstant.CREATOR      -> this.fillStrategy(meta, name, StatementTool.getUserName());
                case FieldsConstant.CREATE_TIME  -> this.fillStrategy(meta, name, LocalDateTime.now());
                default -> {}
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
        if (service != null) {
            service.updateFill(meta);
        }

        String[] names = meta.getGetterNames();
        for (String name : names) {
            switch (name) {
                case FieldsConstant.MODIFIER_ID -> this.fillStrategy(meta, name, StatementTool.getUserId());
                case FieldsConstant.MODIFIER    -> this.fillStrategy(meta, name, StatementTool.getUserName());
                case FieldsConstant.MODIFY_TIME -> this.fillStrategy(meta, name, LocalDateTime.now());
                default -> {}
            }
        }
    }
}
