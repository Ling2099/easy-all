package com.file.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.function.Function;

/**
 * 文件工具类
 *
 * <ol>
 *     <li>{@link #save(File, File)}: 存储/复制文件</li>
 * </ol>
 *
 * @author LZH
 * @version 1.0.7
 * @since 2023-06-09
 */
public class FileTool {

    private static final Logger log = LoggerFactory.getLogger(FileTool.class);

    /**
     * 存储/复制文件
     *
     * <P style="color:red">NIO 形式</P>
     *
     * @param source 源文件对象
     * @param target 目标文件对象
     */
    public static void save(File source, File target) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(source);
        } catch (FileNotFoundException e) {
            log.error("File not found: ", e);
        }
        assert in != null;
        FileChannel input = in.getChannel();

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(target);
        } catch (FileNotFoundException e) {
            log.error("File not found: ", e);
        }

        assert out != null;
        FileChannel output = out.getChannel();

        try {
            input.transferTo(0, input.size(), output);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        } finally {
            try {
                in.close();
                out.close();
                input.close();
                output.close();
            } catch (IOException e) {
                log.error("IO Error: ", e);
            }
        }
    }

    /**
     * 合并分片文件
     *
     */
    public void merge(long total, File target, Function<Long, File> function) {
        int len;
        byte[] bytes = new byte[10_485_760];
        OutputStream output = null;
        InputStream input = null;

        try {
            output = new FileOutputStream(target, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            for (long i = 1; i <= total; i++) {
                File file = function.apply(i);
                input = new FileInputStream(file);
                while ((len = input.read(bytes)) != -1) {
                    // 写入合并的新文件中
                    assert output != null;
                    output.write(bytes, 0, len);
                }
            }
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

}
