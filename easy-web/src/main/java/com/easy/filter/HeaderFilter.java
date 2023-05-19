package com.easy.filter;

import com.orm.tool.ThreadLocalTool;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 可选的 web 过滤器
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-06
 */
public class HeaderFilter implements AsyncHandlerInterceptor {

    /**
     * 请求头拦截器, 并设置至本地线程副本
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler  {@link Object}
     * @return boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 将请求消息头的键值设置至本地线程副本中
        getHeaderMap(request).forEach(ThreadLocalTool::set);

        return true;
    }

    /**
     * 待请求处理完成后, 移除本地线程副本
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param handler  {@link Object}
     * @param ex       {@link Exception}
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) {
        ThreadLocalTool.remove();
    }

    /**
     * 获取请求所有的头（header）信息
     *
     * @param request {@link HttpServletRequest}
     * @return {@link Map}
     */
    private static Map<String, String> getHeaderMap(HttpServletRequest request) {
        // noinspection AlibabaCollectionInitShouldAssignCapacity
        final Map<String, String> headerMap = new HashMap<>();

        final Enumeration<String> names = request.getHeaderNames();
        String name;
        while (names.hasMoreElements()) {
            headerMap.put(name = names.nextElement(), request.getHeader(name));
        }

        return headerMap;
    }
}
