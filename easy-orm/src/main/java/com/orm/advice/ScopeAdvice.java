package com.orm.advice;

import com.orm.domain.PageScope;
import com.orm.tool.StatementTool;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 业务系统数据权限代理类
 *
 * <p>客户端根据当前线程副本所存储的数据权限, 生成指定的 SQL 条件语句</p>
 *
 * <p><b style="color:red">代理命中规则: </b>在 service 层（impl 文件夹的实现类中）, 包含以 find 或 query 关键字开头的方法名</p>
 *
 * <hr/>
 *
 * <p>通过配置项参数 {@code easy.scope} 关闭/开启该代理类的加载</p>
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
@Aspect
@Order(10)
@ConditionalOnProperty(name = "easy.scope")
public class ScopeAdvice {

    public static final Logger log = LoggerFactory.getLogger(ScopeAdvice.class);

    /**
     * 数据权限的 SQL 拼接
     *
     * <p><b style="color:red">注意: </b>在不设置的情况下, 默认查看全部数据权限</p>
     *
     * <hr/>
     *
     * <p>转换规则如下</p>
     *
     * <ol>
     *     <li>全部数据权限（无 SQL 条件）</li>
     *     <li>本级及以下数据权限 （{@code scope LIKE '?%'}）</li>
     *     <li>本级数据权限 （{@code scope = ?}）</li>
     *     <li>仅本人数据权限 （{@code userId = ?}）</li>
     *     <li>自定义数据权限 （{@code orgId IN (?, ?, ?)}）</li>
     * </ol>
     *
     * @param joinPoint {@link JoinPoint}
     */
    @Before("execution(* com..*.service.impl..*.find*(..)) || execution(* com..*.service.impl..*.query*(..))")
    public void doScope(JoinPoint joinPoint) {
        // 获取数据权限
        String scope = StatementTool.getScope();
        // 获取数据权限类型
        Integer scopeType = StatementTool.getScopeType();

        // 条件构建 SQL 条件片段
        switch (scopeType) {
            case 2 -> scope = String.format("scope LIKE '%s%%'", scope);
            case 3 -> scope = String.format("scope = '%s'", scope);
            case 4 -> scope = String.format("userId = '%s'", StatementTool.getUserId());
            case 5 -> scope = String.format("orgId IN (%s)", StatementTool.getOrgIds());
            default -> scope = "";
        }

        invokeChip(joinPoint.getArgs()[0], scope);
    }

    /**
     * 反射执行 {@code setChip} 方法
     *
     * @see com.orm.domain.PageScope#setChip(String)
     * @param obj   {@link com.orm.domain.PageScope} 对象
     * @param scope 数据权限, {@link PageScope#getChip()} 的值
     */
    private void invokeChip(Object obj, String scope) {
        Class<?> clazz = obj.getClass();
        try {
            PropertyDescriptor property = new PropertyDescriptor("chip", clazz);
            Method method = property.getWriteMethod();
            method.invoke(clazz.getDeclaredConstructor().newInstance(), scope);
        } catch (IntrospectionException | InvocationTargetException
                | IllegalAccessException | InstantiationException
                | NoSuchMethodException e) {
            log.error("Reflect error: ", e);
        }
    }

}
