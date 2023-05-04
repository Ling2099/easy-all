package com.orm.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 批量更新
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
public class UpdateBatch extends AbstractMethod {

    /**
     * Constructor Function
     */
    public UpdateBatch() {
        this("updateBatch");
    }

    /**
     * Constructor Function
     *
     * @param methodName 方法名
     * @since 3.5.0
     */
    public UpdateBatch(String methodName) {
        super(methodName);
    }

    /**
     * 重写 "批量更新" 具体实现逻辑
     *
     * @param mapperClass Mapper 类
     * @param modelClass  实体类
     * @param tableInfo   {@link TableInfo}
     * @return {@link MappedStatement}
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass,
                                                 Class<?> modelClass, TableInfo tableInfo) {
        String sql = "<script>\n<foreach collection=\"list\" item=\"item\" separator=\";\">\nupdate %s %s where %s=#{%s} %s\n</foreach>\n</script>";

        String additional = tableInfo.isWithVersion() ?
                tableInfo.getVersionFieldInfo().getVersionOli("item", "item.") :
                "" + tableInfo.getLogicDeleteSql(true, true);

        String setSql = sqlSet(tableInfo.isWithLogicDelete(), false, tableInfo, false, "item", "item.");
        String sqlResult = String.format(sql, tableInfo.getTableName(), setSql, tableInfo.getKeyColumn(), "item." + tableInfo.getKeyProperty(), additional);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);

        return this.addUpdateMappedStatement(mapperClass, modelClass, "updateBatch", sqlSource);
    }
}
