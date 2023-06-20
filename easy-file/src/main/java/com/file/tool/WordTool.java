package com.file.tool;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.file.template.AbstractStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Word 文件导出
 *
 * <ol>
 *     <li>导出纯表单的 word 文本文件: {@link #export(String, InputStream, Map, HttpServletResponse)}</li>
 *     <li>导出表单、表格的 word 文本文件: {@link #export(String, InputStream, Map, Map, HttpServletResponse)}</li>
 *     <li>导出表单、图片的 word 文本文件: {@link #export(String, InputStream, Map, HttpServletResponse, Map)}</li>
 *     <li>导出表单、表格、图片的 word 文本文件: {@link #export(String, InputStream, Map, Map, Map, HttpServletResponse)}</li>
 *     <li>自定义 word 文本文件导出逻辑: {@link #export(String, HttpServletResponse, Consumer)}</li>
 * </ol>
 *
 * @author LZH
 * @version 1.0.7
 * @since 2023-06-19
 */
@SuppressWarnings("ConstantConditions")
public final class WordTool extends AbstractStream {

    private static final Logger log = LoggerFactory.getLogger(WordTool.class);

    /**
     * 导出纯表单的 word 文本文件
     *
     * @param fileName 导出的文件名
     * @param is       文件模板
     * @param form     待填充的数据
     * @param response {@link HttpServletResponse}
     */
    public static void export(String fileName, InputStream is,
                                  Map<String, Object> form, HttpServletResponse response) {
        invoker(fileName, response, os -> {
            try {
                XWPFTemplate.compile(is).render(form).write(os);
            } catch (IOException e) {
                log.error("IO Error: ", e);
            }
        });
    }

    /**
     * 导出表单、表格的 word 文本文件
     *
     * @param fileName 导出的文件名
     * @param is       文件模板
     * @param form     表单数据
     * @param table    表格数据
     * @param response {@link HttpServletResponse}
     * @param <V>      泛型
     */
    public static <V> void export(String fileName, InputStream is,
                                  Map<String, Object> form, Map<String, V> table, HttpServletResponse response) {
        invoker(fileName, response, os -> {
            LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
            ConfigureBuilder builder = Configure.builder();

            // 绑定表格
            table.forEach((k, v) -> builder.bind(k, policy));
            Configure config = builder.build();
            // 合并集合
            form.putAll(table);

            try {
                XWPFTemplate.compile(is, config).render(form).write(os);
            } catch (IOException e) {
                log.error("IO Error: ", e);
            }
        });
    }

    /**
     * 导出表单、图片的 word 文本文件
     *
     * @param fileName 导出文件名
     * @param is       文件模板
     * @param form     表单数据
     * @param response {@link HttpServletResponse}
     * @param picture  图片数据
     * @param <T>      泛型
     */
    public static <T> void export(String fileName, InputStream is, Map<String, Object> form,
                                  HttpServletResponse response, Map<String, byte[]> picture) {
        invoker(fileName, response, os -> {
            picture.forEach((k, v) -> form.put(k, Pictures.ofBytes(v).create()));
            try {
                XWPFTemplate.compile(is).render(form).write(os);
            } catch (IOException e) {
                log.error("IO Error: ", e);
            }
        });
    }

    /**
     * 导出表单、表格、图片的 word 文本文件
     *
     * @param fileName 导出文件名
     * @param is       文件模板
     * @param form     表单数据
     * @param table    表格数据
     * @param picture  图片数据
     * @param response {@link HttpServletResponse}
     * @param <V>      泛型
     */
    public static <V> void export(String fileName, InputStream is, Map<String, Object> form,
                                  Map<String, V> table, Map<String, byte[]> picture,
                                  HttpServletResponse response) {
        invoker(fileName, response, os -> {
            LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
            ConfigureBuilder builder = Configure.builder();

            // 绑定表格
            table.forEach((k, v) -> builder.bind(k, policy));
            // 绑定图片
            picture.forEach((k, v) -> form.put(k, Pictures.ofBytes(v).create()));
            // 合并集合
            form.putAll(table);

            Configure config = builder.build();
            try {
                XWPFTemplate.compile(is, config).render(form).write(os);
            } catch (IOException e) {
                log.error("IO Error: ", e);
            }
        });
    }

    /**
     * 自定义 word 文本文件导出逻辑
     *
     * @param fileName 导出文件名
     * @param response {@link HttpServletResponse}
     * @param consumer 自定义函数式接口
     */
    public static void export(String fileName, HttpServletResponse response, Consumer<OutputStream> consumer) {
        invoker(fileName, response, consumer);
    }

}
