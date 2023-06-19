package com.file.tool;

import com.deepoove.poi.XWPFTemplate;
import com.file.template.AbstractStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Word 文件导出
 *
 * <ol>
 *     <li>导出纯表单的 word 文本文件: </li>
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
     * @param data     待填充的数据
     * @param response {@link HttpServletResponse}
     * @param <T>      泛型
     */
    public static <T> void export(String fileName, InputStream is,
                                  Map<String, T> data, HttpServletResponse response) {
        setHeader(fileName, response);
        OutputStream os = getStream(response);

        try {
            XWPFTemplate.compile(is).render(data).write(os);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }

        close(os, response);
    }

}
