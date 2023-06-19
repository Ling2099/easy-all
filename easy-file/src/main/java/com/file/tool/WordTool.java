package com.file.tool;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.util.PoitlIOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * Word 文件导出
 *
 * <ol>
 *     <li>导出纯表单的 word 文本文件: {@link #export(byte[], Map, HttpServletResponse)}</li>
 * </ol>
 *
 * @author LZH
 * @version 1.0.7
 * @since 2023-06-19
 */
public final class WordTool {

    private static final Logger log = LoggerFactory.getLogger(WordTool.class);

    /**
     * 导出纯表单的 word 文本文件
     *
     * @param bytes    文件模板
     * @param data     待填充的数据
     * @param response {@link HttpServletResponse}
     * @param <T>      泛型
     */
    public static <T> void export(byte[] bytes, Map<String, T> data, HttpServletResponse response) {
        XWPFTemplate temp = XWPFTemplate.compile(new ByteArrayInputStream(bytes)).render(data);
        writeAndClose(temp, response);
    }

    /**
     * 写入流并关闭
     *
     * @param temp     {@link XWPFTemplate}
     * @param response {@link HttpServletResponse}
     */
    private static void writeAndClose(XWPFTemplate temp, HttpServletResponse response) {
        try {
            OutputStream out = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(out);
            temp.write(bos);

            bos.flush();
            out.flush();
            PoitlIOUtils.closeQuietlyMulti(temp, bos, out);
        } catch (IOException e) {
            log.error("I/O Exception: ", e);
        }
    }

}
