package com.file.tool;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.file.conf.ExcelListener;
import com.file.domain.Head;
import com.file.template.AbstractStream;
import org.apache.commons.math3.util.Pair;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>Excel 文件导出</p>
 * <ol>
 *     <li>导出 Excel 文件: {@link #export(String, List, HttpServletResponse)}</li>
 *     <li>导出 Excel 文件（带模板）: {@link #export(String, InputStream, List, HttpServletResponse)}</li>
 *     <li>导出 Excel 文件（带模板、额外填充数据）: {@link #export(String, InputStream, List, Object, boolean, HttpServletResponse)}</li>
 *     <li>导出 Excel 文件（自定义导出逻辑）: {@link #export(String, HttpServletResponse, Consumer)}</li>
 * </ol>
 *
 * <hr/>
 *
 * <p>Excel 文件解析</p>
 * <ol>
 *     <li>解析 Excel 文件: {@link #parse(InputStream, Class)}</li>
 *     <li>解析 Excel 文件: {@link #parse(InputStream, Class, Consumer)}</li>
 *     <li>解析 Excel 文件: {@link #parse(InputStream, Class, int, Consumer)}</li>
 *     <li>解析 Excel 文件: {@link #parse(InputStream, Class, int)}（<b style="color:red">复杂文件</b>）</li>
 * </ol>
 *
 * @author LZH
 * @version 1.0.5
 * @since 2023-05-23
 */
@SuppressWarnings("ConstantConditions")
public final class ExcelTool extends AbstractStream {

    /**
     * 由数据集合 {@code list} 导出 Excel 文件
     *
     * @param fileName  导出的文件名
     * @param list      数据集合
     * @param response  {@link HttpServletResponse}
     * @param <T>       泛型
     */
    public static <T> void export(String fileName, List<T> list, HttpServletResponse response) {
        setHeader(fileName, response);

        Class<?> clazz = list.get(0).getClass();
        OutputStream os = getStream(response);

        EasyExcel.write(os, clazz)
                .sheet()
                .doWrite(list);

        close(os, response);
    }

    /**
     * 由数据集合 {@code list} 与模板文件 {@code template} 导出 Excel 文件
     *
     * @param fileName  导出的文件名
     * @param template  Excel 模板文件字节数组
     * @param list      数据集合
     * @param response  {@link HttpServletResponse}
     * @param <T>       泛型
     */
    public static <T> void export(String fileName, InputStream template,
                                  List<T> list, HttpServletResponse response) {
        setHeader(fileName, response);
        OutputStream os = getStream(response);

        EasyExcel.write(os)
                .withTemplate(template)
                .sheet()
                .doFill(list);

        close(os, response);
    }

    /**
     * 由数据集合 {@code list}、模板文件 {@code template} 与额外的数据对象导出 Excel 文件
     *
     * @param fileName 导出的文件名
     * @param template Excel 模板文件字节数组
     * @param list     数据集合
     * @param v        额外的填充对象
     * @param newRow   每次使用列表参数时是否创建一个新行
     * @param response {@link HttpServletResponse}
     * @param <T>      数据集合泛型
     * @param <V>      额外数据泛型
     */
    public static <T, V> void export(String fileName, InputStream template,
                                     List<T> list, V v,
                                     boolean newRow, HttpServletResponse response) {
        setHeader(fileName, response);
        OutputStream os = getStream(response);

        // 创建操作对象
        ExcelWriter writer = EasyExcel
                .write(os)
                .withTemplate(template)
                .build();

        WriteSheet sheet = EasyExcel.writerSheet().build();
        FillConfig config = FillConfig.builder().forceNewRow(newRow).build();
        // 填充列表、对象
        writer.fill(list, config, sheet).fill(v, config, sheet);

        writer.finish();
        close(os, response);
    }

    /**
     * 自定义逻辑导出 Excel 文件
     *
     * @param fileName 导出的文件名
     * @param response {@link HttpServletResponse}
     * @param consumer 自定义函数式接口
     */
    public static void export(String fileName,
                              HttpServletResponse response,
                              Consumer<OutputStream> consumer) {
        setHeader(fileName, response);
        OutputStream os = getStream(response);

        consumer.accept(os);
        close(os, response);
    }

    /**
     * 解析 Excel 文件
     *
     * @param in    Excel 文件输入流
     * @param clazz 实体类型
     * @param <T>   泛型
     * @return 数据集合
     */
    public static <T> List<T> parse(InputStream in, Class<T> clazz) {
        ExcelListener<T> listener = new ExcelListener<>();
        EasyExcel.read(in, clazz, listener).sheet().doRead();
        return listener.getData();
    }

    /**
     * 解析 Excel 文件
     *
     * @param in       Excel 文件输入流
     * @param clazz    实体类型
     * @param consumer 客户端自定义方法
     * @param <T>      泛型
     */
    public static <T> void parse(InputStream in, Class<T> clazz, Consumer<List<T>> consumer) {
        ExcelListener<T> listener = new ExcelListener<>(consumer);
        EasyExcel.read(in, clazz, listener).sheet().doReadSync();
    }

    /**
     * 解析 Excel 文件
     *
     * @param in        Excel 文件输入流
     * @param clazz     实体类型
     * @param batchSize 文件分批解析时的阈值
     * @param consumer  客户端自定义方法
     * @param <T>       泛型
     */
    public static <T> void parse(InputStream in, Class<T> clazz,
                                 int batchSize, Consumer<List<T>> consumer) {
        ExcelListener<T> listener = new ExcelListener<>(batchSize, consumer);
        EasyExcel.read(in, clazz, listener).sheet().doReadSync();
    }

    /**
     * 解析 Excel 文件（<b style="color:red">复杂文件</b>）
     *
     * @param in        Excel 文件输入流
     * @param clazz     实体类型
     * @param rowNumber 从第几行开始解析
     * @param <T>       泛型
     * @return {@link Pair}
     * <p>返回数据说明</p>
     * <ul>
     *     <li>{@link Pair#getKey()}: Excel 文件表格数据</li>
     *     <li>
     *         {@link Pair#getValue()}: Excel 文件头部数据
     *         <ul>
     *             <li>{@link Head#getRow()}: 行</li>
     *             <li>{@link Head#getCol()}: 列</li>
     *             <li>{@link Head#getVal()}: 文本数据</li>
     *         </ul>
     *     </li>
     * </ul>
     */
    public static <T> Pair<List<T>, List<Head<?>>> parse(InputStream in, Class<T> clazz, int rowNumber) {
        ExcelListener<T> listener = new ExcelListener<>();
        EasyExcel.read(in, clazz, listener)
                .sheet()
                .headRowNumber(rowNumber)
                .doRead();
        return Pair.create(listener.getData(), listener.getHead());
    }

}
