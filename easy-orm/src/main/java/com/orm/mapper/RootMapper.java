package com.orm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.orm.domain.PageEntity;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得 CRUD 功能
 *
 * <p>继承自 {@link com.baomidou.mybatisplus.core.mapper.BaseMapper}</p>
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
@SuppressWarnings({"UnusedReturnValue", "unchecked"})
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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    default void updateBatch(Collection<T> list, int batchSize) {
        Assert.isFalse(batchSize < 1, "batchSize must not be less than one");
        split(list, batchSize).forEach(this::updateBatch);
    }

    /**
     * 批量操作
     *
     * @param list      数据集合
     * @param batchSize 批次大小
     * @param consumer  {@link BiConsumer}
     * @param <E>       泛型
     * @return 是否成功
     * @since 1.0.11
     */
    default <E> boolean executeBatch(Collection<E> list, int batchSize, BiConsumer<SqlSession, E> consumer) {
        Class<?> entity = ReflectionKit.getSuperClassGenericType(this.getClass(), ServiceImpl.class, 1);
        Log log = LogFactory.getLog(RootMapper.class);
        return SqlHelper.executeBatch(entity, log, list, batchSize, consumer);
    }

    /**
     * 批量更新, 根据主键 ID
     *
     * @param list 数据集合
     * @return 是否成功
     * @since 1.0.11
     */
    default boolean updateBatchById(Collection<T> list) {
        return this.updateBatchById(list, 1000);
    }

    /**
     * 批量更新, 根据主键 ID
     *
     * @param list      数据集合
     * @param batchSize 批次大小
     * @return 是否成功
     * @since 1.0.11
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean updateBatchById(Collection<T> list, int batchSize) {
        Class<?> mapper = ReflectionKit.getSuperClassGenericType(this.getClass(), ServiceImpl.class, 0);
        String sqlStatement = SqlHelper.getSqlStatement(mapper, SqlMethod.UPDATE_BY_ID);
        return this.executeBatch(list, batchSize, (sqlSession, entity) -> {
            MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put("et", entity);
            sqlSession.update(sqlStatement, param);
        });
    }

    /**
     * 批量更新或插入
     *
     * @param list 数据集合
     * @return 是否成功
     * @since 1.0.11
     */
    default boolean saveOrUpdateBatch(Collection<T> list) {
        return saveOrUpdateBatch(list, 1000);
    }

    /**
     * 批量更新或插入
     *
     * @param list      数据集合
     * @param batchSize 批次大小
     * @return 是否成功
     * @since 1.0.11
     */
    default boolean saveOrUpdateBatch(Collection<T> list, int batchSize) {
        Class<?> entityClass = ReflectionKit.getSuperClassGenericType(this.getClass(), ServiceImpl.class, 1);
        Class<?> mapperClass = ReflectionKit.getSuperClassGenericType(this.getClass(), ServiceImpl.class, 0);
        Log log = LogFactory.getLog(RootMapper.class);

        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
        return SqlHelper.saveOrUpdateBatch(entityClass, mapperClass, log, list, batchSize, (sqlSession, entity) -> {
            Object idVal = tableInfo.getPropertyValue(entity, keyProperty);
            return StringUtils.checkValNull(idVal)
                    || CollectionUtils.isEmpty(sqlSession.selectList(SqlHelper.getSqlStatement(mapperClass, SqlMethod.SELECT_BY_ID), entity));
        }, (sqlSession, entity) -> {
            MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            sqlSession.update(SqlHelper.getSqlStatement(mapperClass, SqlMethod.UPDATE_BY_ID), param);
        });
    }

    /**
     * 批量更新或插入（带条件）
     *
     * <p style="color:red">非必要勿调用此方法（数据量少可斟酌）, 原因见代码</p>
     *
     * @param list          实体对象集合
     * @param updateWrapper {@link Wrapper}
     */
    default void saveOrUpdateBatch(Collection<T> list, Wrapper<T> updateWrapper) {
        list.forEach(entity -> saveOrUpdate(entity, updateWrapper));
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
     * @since 1.0.11
     */
    default <P extends PageEntity> IPage<T> page(P page, Wrapper<T> queryWrapper) {
        Page<T> p = new Page<>(page.getPageNum(), page.getPageSize());
        // 正序
        if (page.isNotBlankAscs()) {
            p.addOrder(OrderItem.ascs(page.getAscs()));
        }
        // 倒序
        if (page.isNotBlankDescs()) {
            p.addOrder(OrderItem.descs(page.getDescs()));
        }
        return selectPage(p, queryWrapper);
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
     * 查询数据（带条件）, 并转换
     *
     * @param queryWrapper {@link Wrapper}
     * @param mapper       {@link Function}
     * @param <V>          泛型
     * @return {@link List}
     * @since 1.0.11
     */
    default <V> List<V> list(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
        return selectObjs(queryWrapper).stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
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
