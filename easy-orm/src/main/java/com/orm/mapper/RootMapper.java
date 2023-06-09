package com.orm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.orm.domain.PageEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得 CRUD 功能
 *
 * <p>继承自 {@link com.baomidou.mybatisplus.core.mapper.BaseMapper}</p>
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
@SuppressWarnings("UnusedReturnValue")
public interface RootMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入
     *
     * @param list 实体对象集合
     * @return 成功写入条数
     */
    int insertBatch(Collection<T> list);

    /**
     * 批量更新
     *
     * @param list 实体对象集合
     * @return 成功写入条数
     */
    int updateBatch(Collection<T> list);

    /**
     * 批量插入
     *
     * @param list      实体对象集合
     * @param batchSize 批次大小
     */
    default void insertBatch(Collection<T> list, int batchSize) {
        Assert.isFalse(batchSize < 1, "batchSize must not be less than one");
        split(list, batchSize).forEach(this::insertBatch);
    }

    /**
     * 批量更新
     *
     * @param list      实体对象集合
     * @param batchSize 批次大小
     */
    default void updateBatch(Collection<T> list, int batchSize) {
        Assert.isFalse(batchSize < 1, "batchSize must not be less than one");
        split(list, batchSize).forEach(this::updateBatch);
    }

    /**
     * 批量更新, 根据主键 ID
     *
     * <p style="color:red">非必要勿调用此方法（数据量少可斟酌）, 原因见代码</p>
     *
     * @param list 实体对象集合
     */
    default void updateBatchById(Collection<T> list) {
        list.forEach(this::updateById);
    }

    /**
     * 批量更新或插入
     *
     * <p style="color:red">非必要勿调用此方法（数据量少可斟酌）, 原因见代码</p>
     *
     * @param list 实体对象集合
     */
    default void saveOrUpdateBatch(Collection<T> list) {
        list.forEach(this::saveOrUpdate);
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     * @return boolean
     */
    default boolean saveOrUpdate(T entity) {
        if (null != entity) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
            Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
            String keyProperty = tableInfo.getKeyProperty();
            Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
            Object idVal = tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());
            return (null == idVal ? insert(entity) : updateById(entity)) >= 1;
        }
        return false;
    }

    /**
     * 根据 {@link Wrapper} 尝试更新, 否继续执行 {@link #saveOrUpdate(Object)} 方法
     *
     * @param entity        实体类
     * @param updateWrapper {@link Wrapper}
     * @return boolean
     */
    default boolean saveOrUpdate(T entity, Wrapper<T> updateWrapper) {
        return update(entity, updateWrapper) >= 1 || saveOrUpdate(entity);
    }

    /**
     * 分页查询, <b style="color:red">推荐此方法</b>
     *
     * @param page         分页对象 {@link PageEntity}
     * @param queryWrapper 条件构造器 {@link Wrapper}
     * @param <P>          泛型, 必须继承至 {@link PageEntity}
     * @return 分页查询结果 {@link IPage}
     */
    default <P extends PageEntity> IPage<T> page(P page, Wrapper<T> queryWrapper) {
        return selectPage(new Page<>(page.getPageNum(), page.getPageSize()), queryWrapper);
    }

    /**
     * 查询所有数据
     *
     * @return {@link List}
     */
    default List<T> list() {
        return selectList(Wrappers.emptyWrapper());
    }

    /**
     * 对集合按照指定长度分段，每一个段为单独的集合，返回这个集合的列表
     *
     * @param collection 集合
     * @param size       每个段的长度
     * @return 分段列表
     */
    default List<List<T>> split(Collection<T> collection, int size) {
        final List<List<T>> result = new ArrayList<>();
        if (collection == null || collection.isEmpty()) {
            return result;
        }

        ArrayList<T> subList = new ArrayList<>(size);
        for (T t : collection) {
            if (subList.size() >= size) {
                result.add(subList);
                subList = new ArrayList<>(size);
            }
            subList.add(t);
        }
        result.add(subList);
        return result;
    }

}
