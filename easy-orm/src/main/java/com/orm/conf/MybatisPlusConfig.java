package com.orm.conf;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.orm.methods.InsertBatch;
import com.orm.methods.UpdateBatch;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * MyBatis Plus 配置类
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023/05/02
 */
@MapperScan("com.**.mapper")
public class MybatisPlusConfig {

    /**
     * MyBatis Plus 分页拦截器
     *
     * @return {@link MybatisPlusInterceptor}
     * @since 1.0.6
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
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
}
