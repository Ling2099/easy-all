package com.file.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.function.Consumer;

/**
 * 流相关操作类
 *
 * @author LZH
 * @version 1.0.7
 * @since 2023-06-19
 */
public abstract class AbstractStream {

    private static final Logger log = LoggerFactory.getLogger(AbstractStream.class);

    /**
     * 调用程序
     *
     * @param fileName 文件名
     * @param response {@link HttpServletResponse}
     * @param consumer {@link Consumer}
     */
    protected static void invoker(String fileName, HttpServletResponse response,
                                 Consumer<OutputStream> consumer) {
        setHeader(fileName, response);
        OutputStream os = getStream(response);
        consumer.accept(os);
        // noinspection ConstantConditions
        close(os, response);
    }

    /**
     * 设置响应头信息
     *
     * @param fileName 导出的文件名
     * @param response {@link HttpServletResponse}
     */
    private static void setHeader(String fileName, HttpServletResponse response) {
        try {
            fileName = String.format("attachment; filename=%s", URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("String Encode: ", e);
        }

        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", fileName);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "max-age=0");
    }

    /**
     * 获取流
     *
     * @param response {@link HttpServletResponse}
     * @return {@link OutputStream}
     */
    private static OutputStream getStream(HttpServletResponse response) {
        try {
            return response.getOutputStream();
        } catch (IOException e) {
            log.error("I/O Exception: ", e);
        }
        return null;
    }

    /**
     * 关闭流
     *
     * @param os {@link OutputStream}
     */
    private static void close(OutputStream os, HttpServletResponse response) {
        try {
            assert os != null;
            os.close();
            response.flushBuffer();
        } catch (IOException e) {
            log.error("I/O Exception: ", e);
        }
    }

}
