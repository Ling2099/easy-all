package com.orm.conf;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.orm.constants.FieldsConstant;
import com.orm.methods.InsertBatch;
import com.orm.methods.UpdateBatch;
import com.orm.tool.FieldsTool;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MyBatis Plus 配置类
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
@MapperScan("com.**.mapper")
public class MybatisPlusConfig implements MetaObjectHandler {

    /**
     * MyBatis Plus 分页拦截器
     *
     * @return {@link MybatisPlusInterceptor}
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 注入自定义 Mapper 接口配置
     *
     * <p>将自定义的 Mapper 接口方法添加至 SQL 注入器</p>
     *
     * @return {@link DefaultSqlInjector}
     */
    @Bean
    public DefaultSqlInjector customizedSqlInjector() {
        return new DefaultSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
                List<AbstractMethod> list = super.getMethodList(mapperClass, tableInfo);
                list.add(new InsertBatch());
                list.add(new UpdateBatch());
                return list;
            }
        };
    }

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
                case FieldsConstant.ORG_ID       -> this.fillStrategy(meta, name, 1);
                case FieldsConstant.SCOPE        -> this.fillStrategy(meta, name, FieldsTool.getScope());
                case FieldsConstant.HAS_DEL      -> this.fillStrategy(meta, name, 0);
                case FieldsConstant.CREATOR_ID   -> this.fillStrategy(meta, name, FieldsTool.getUserId());
                case FieldsConstant.CREATOR      -> this.fillStrategy(meta, name, FieldsTool.getUserName());
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
        String[] names = meta.getGetterNames();
        for (String name : names) {
            switch (name) {
                case FieldsConstant.MODIFIER_ID -> this.fillStrategy(meta, name, FieldsTool.getUserId());
                case FieldsConstant.MODIFIER    -> this.fillStrategy(meta, name, FieldsTool.getUserName());
                case FieldsConstant.MODIFY_TIME -> this.fillStrategy(meta, name, LocalDateTime.now());
                default -> {}
            }
        }
    }
}
