package com.file.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * 文件工具类
 *
 * @author LZH
 * @version 1.0.7
 * @since 2023-06-09
 */
public class FileTool {

    private static final Logger log = LoggerFactory.getLogger(FileTool.class);

    public static void saveChunk(byte[] source, String target) {
        File file = new File("");

        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert output != null;
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);

        try {
            bufferedOutput.write(source);
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveChunk(file, new File(target));

        try {
            output.close();
            bufferedOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveChunk(File source, File target) {
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
        }

        try {
            in.close();
            out.close();
            input.close();
            output.close();
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }

    }

    public static void main(String[] args) {
        File source = new File("/home/lzh/1.txt");
        File target = new File("/home/lzh/2.txt");

        saveChunk(source, target);
    }

}
