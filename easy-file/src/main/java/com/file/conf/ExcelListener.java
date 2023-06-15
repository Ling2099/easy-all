package com.file.conf;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.file.domain.Head;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Excel 文件读监听器
 *
 * @author LZH
 * @version 1.0.7
 * @since 2023-06-14
 */
public class ExcelListener<T> implements ReadListener<T> {

    /**
     * 解析数据承载集合
     */
    private List<T> data;

    /**
     * 解析头部数据承载集合
     */
    private List<Head> head;

    /**
     * 集合达到此阈值时, 将会执行客户端自定义方法 {@link #consumer}
     */
    private int batchSize;

    /**
     * 客户端自定义方法
     */
    private Consumer<List<T>> consumer;

    /**
     * 无参构造器, 创建 {@link #data}、{@link #head} 集合对象
     */
    public ExcelListener() {
        this.data = new ArrayList<>();
        this.head = new ArrayList<>();
    }

    /**
     * 带参构造器, 初始化客户端自定义方法
     *
     * @param consumer {@link #consumer}
     */
    public ExcelListener(Consumer<List<T>> consumer) {
        this.consumer = consumer;
        this.data = new ArrayList<>();
    }

    /**
     * 带参构造器, 初始化集合阈值与客户端自定义方法
     *
     * @param batchSize {@link #batchSize}
     * @param consumer  {@link #consumer}
     */
    public ExcelListener(int batchSize, Consumer<List<T>> consumer) {
        this.batchSize = batchSize;
        this.consumer = consumer;
        this.data = new ArrayList<>(batchSize);
    }

    /**
     * 每条数据解析都会调用此方法
     *
     * @param t       集合元素
     * @param context {@link AnalysisContext}
     */
    @Override
    public void invoke(T t, AnalysisContext context) {
        data.add(t);
        if (batchSize != 0 && data.size() >= batchSize) {
            consumer.accept(data);
            data = new ArrayList<>(batchSize);
        }
    }

    /**
     * 解析 Excel 表格的头部数据
     *
     * @param headMap {@link Map}
     * @param context {@link AnalysisContext}
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        if (head != null) {
            headMap.forEach((k, v) -> head.add(new Head(v.getRowIndex(), v.getColumnIndex(), v.getStringValue())));
        }
    }

    /**
     * 所有数据解析完成后, 调用此方法
     *
     * @param context {@link AnalysisContext}
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (consumer != null) {
            consumer.accept(data);
        }
    }

    /**
     * getter function
     *
     * @return {@link #data}
     */
    public List<T> getData() {
        return data;
    }

    /**
     * getter function
     *
     * @see Head
     * @return {@link #head}
     */
    public List<Head> getHead() {
        return head;
    }
}
