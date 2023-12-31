package com.orm.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 批量插入
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023/05/02
 */
public class InsertBatch extends AbstractMethod {

    /**
     * Constructor Function
     */
    public InsertBatch() {
        this("insertBatch");
    }

    /**
     * Constructor Function
     *
     * @param name 方法名
     * @since 3.5.0
     */
    public InsertBatch(String name) {
        super(name);
    }

    /**
     * 重写 "批量插入" 具体实现逻辑
     *
     * @param mapperClass Mapper 类
     * @param modelClass  实体类
     * @param tableInfo   {@link TableInfo}
     * @return {@link MappedStatement}
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass,
                                                 Class<?> modelClass, TableInfo tableInfo) {

        final String baseSql = "<script>insert into %s %s values %s</script>";
        final String field = this.buildField(tableInfo);
        final String value = this.buildValue(tableInfo);

        final String resultSql = String.format(baseSql, tableInfo.getTableName(), field, value);
        SqlSource source = languageDriver.createSqlSource(configuration, resultSql, modelClass);

        return this.addInsertMappedStatement(
            mapperClass, modelClass, "insertBatch", source, new NoKeyGenerator(), null, null
        );
    }

    /**
     * 构建 INSERT 语句的字段部分
     *
     * @param tableInfo {@link TableInfo}
     * @return {@link String} 已构建好的字符串
     */
    private String buildField(TableInfo tableInfo) {
        StringBuilder builder = new StringBuilder(tableInfo.getKeyColumn());
        builder.append(COMMA);
        tableInfo.getFieldList().forEach(v -> builder.append(v.getColumn()).append(COMMA));
        builder.delete(builder.length() - 1, builder.length());
        builder.insert(0, LEFT_BRACKET);
        builder.append(RIGHT_BRACKET);
        return builder.toString();
    }

    /**
     * 构建 INSERT 语句的 value 部分
     *
     * @param tableInfo {@link TableInfo}
     * @return {@link String}
     */
    private String buildValue(TableInfo tableInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("<foreach collection=\"list\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\">");
        builder.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
        tableInfo.getFieldList().forEach(v -> builder.append("#{item.").append(v.getProperty()).append("},"));
        builder.delete(builder.length() - 1, builder.length());
        builder.append("</foreach>");
        return builder.toString();
    }
}
