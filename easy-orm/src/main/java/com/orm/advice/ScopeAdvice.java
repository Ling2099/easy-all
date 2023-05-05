package com.orm.advice;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.orm.tool.FieldsTool;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;

/**
 * 业务系统数据权限代理类
 *
 * <p>客户端根据当前线程副本所存储的数据权限, 生成指定的 SQL 条件语句</p>
 *
 * <p><b style="color:red">代理命中规则: </b>在 service 层（impl 文件夹的实现类中）, 包含以 find 或 query 关键字开头的方法名</p>
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
@Aspect
@Order(10)
@ConditionalOnProperty(name = "easy.scope")
public class ScopeAdvice {

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
        String scope = FieldsTool.getScope();
        // 获取数据权限类型
        Integer scopeType = FieldsTool.getScopeType();

        // 条件构建 SQL 条件片段
        switch (scopeType) {
            case 2 -> scope = StrUtil.format("scope LIKE '{}%'", scope);
            case 3 -> scope = StrUtil.format("scope = '{}'", scope);
            case 4 -> scope = StrUtil.format("userId = '{}'", FieldsTool.getUserId());
            case 5 -> scope = StrUtil.format("orgId IN ({})", FieldsTool.getOrgIds());
            default -> scope = "";
        }

        // 此时被代理的入参参数中, 成员变量已被赋值
        ReflectUtil.invoke(joinPoint.getArgs()[0], "setChip", scope);
    }

}
