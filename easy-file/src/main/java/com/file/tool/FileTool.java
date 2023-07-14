package com.file.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 *
 * <ol>
 *     <li>{@link #copy(String, String)}: 文件复制</li>
 *     <li>{@link #write(String, String)}: 文件写入</li>
 *     <li>{@link #write(byte[], String)}: 文件写入</li>
 *     <li>{@link #readString(String)}: 文件读取</li>
 *     <li>{@link #readBytes(String)}: 文件读取</li>
 *     <li>{@link #split(File, int)}: 文件分片</li>
 *     <li>{@link #merge(File[], File)}: 文件合并</li>
 *     <li>{@link #zip(String, String)}: 单文件压缩</li>
 *     <li>{@link #zip(String[], String)}: 多文件压缩</li>
 *     <li>{@link #unzip(String, String)}: 文件解压</li>
 *     <li>{@link #convert(long)}: 文件单位转换</li>
 *     <li>{@link #getBytes(InputStream)}: {@link InputStream} 转字节数组</li>
 * </ol>
 *
 * @author LZH
 * @version 1.0.7
 * @since 2023-06-09
 */
public class FileTool {

    /**
     * 日志对象
     */
    private static final Logger log = LoggerFactory.getLogger(FileTool.class);

    /**
     * 文件复制
     *
     * @param source 源文件地址
     * @param target 目标文件地址
     */
    public static void copy(String source, String target) {
        try {
            Files.copy(Paths.get(source), Paths.get(target));
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 文件写入
     *
     * @param content 被写入的内容
     * @param path    文件地址
     */
    public static void write(String content, String path) {
        write(content.getBytes(), path);
    }

    /**
     * 文件写入
     *
     * @param content 被写入的内容
     * @param path    文件地址
     */
    public static void write(byte[] content, String path) {
        Path file = Paths.get(path);
        try {
            Files.write(file, content);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 文件读取
     *
     * @param path 文件地址
     * @return 文件内容
     */
    public static String readString(String path) {
        byte[] bytes = readBytes(path);
        // noinspection ConstantConditions
        return new String(bytes);
    }

    /**
     * 文件读取
     *
     * @param path 文件地址
     * @return 文件内容
     */
    public static InputStream readStream(String path) {
        File file = new File(path);
        try (FileInputStream fis = new FileInputStream(file);
             FileChannel fileChannel = fis.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());
            fileChannel.read(buffer);
            buffer.flip();
            return new ByteArrayInputStream(buffer.array());
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
        return null;
    }

    /**
     * 文件读取
     *
     * @param path 文件地址
     * @return 文件内容
     */
    public static byte[] readBytes(String path) {
        Path file = Paths.get(path);
        try {
             return Files.readAllBytes(file);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
        return null;
    }

    /**
     * 文件分片
     *
     * @param source    源文件
     * @param chunkSize 分片大小
     */
    public static void split(File source, int chunkSize) {
        byte[] buffer = new byte[chunkSize];
        String fileName = source.getName();
        int partCounter = 0;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(source))) {
            int bytesAmount = 0;
            while ((bytesAmount = bis.read(buffer)) > 0) {
                String filePartName = String.format("%s.%03d", fileName, partCounter++);
                File newFile = new File(source.getParent(), filePartName);
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytesAmount);
                }
            }
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 合并分片文件
     *
     * @param sources 源文件数组
     * @param target  合并后的目标文件
     */
    public static void merge(File[] sources, File target) {
        try (FileOutputStream fos = new FileOutputStream(target);
             FileChannel dest = fos.getChannel()) {
            // 文件排序
            List<File> list = Arrays
                .stream(sources)
                .sorted()
                .collect(Collectors.toList());

            for (File file : list) {
                try (FileInputStream fis = new FileInputStream(file);
                     FileChannel src = fis.getChannel()) {
                    src.transferTo(0, src.size(), dest);
                }
            }
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 单文件压缩
     *
     * @param source 源文件地址
     * @param target 压缩后的目标文件地址
     */
    public static void zip(String source, String target) {
        Path file = Paths.get(source);
        Path zip = Paths.get(target);

        try (ZipOutputStream outputStream = new ZipOutputStream(Files.newOutputStream(zip))) {
            ZipEntry entry = new ZipEntry(file.getFileName().toString());
            outputStream.putNextEntry(entry);
            byte[] bytes = Files.readAllBytes(file);
            outputStream.write(bytes, 0, bytes.length);
            outputStream.closeEntry();
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 多文件压缩
     *
     * @param source 源文件地址
     * @param target 压缩后的目标文件地址
     */
    public static void zip(String[] source, String target) {
        List<Path> list = Arrays
            .stream(source)
            .map(Paths::get)
            .collect(Collectors.toList());
        Path zip = Paths.get(target);

        try (ZipOutputStream outputStream = new ZipOutputStream(Files.newOutputStream(zip))) {
            for (Path path : list) {
                ZipEntry entry = new ZipEntry(path.getFileName().toString());
                outputStream.putNextEntry(entry);
                byte[] bytes = Files.readAllBytes(path);
                outputStream.write(bytes, 0, bytes.length);
                outputStream.closeEntry();
            }
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 文件解压
     *
     * @param source 源压缩文件
     * @param target 解压目录
     */
    public static void unzip(String source, String target) {
        Path zip = Paths.get(source);
        Path dir = Paths.get(target);

        try (ZipInputStream inputStream = new ZipInputStream(Files.newInputStream(zip))) {
            ZipEntry entry = inputStream.getNextEntry();
            while (entry != null) {
                Path targetPath = dir.resolve(entry.getName());
                if (!entry.isDirectory()) {
                    Files.copy(inputStream, targetPath);
                } else {
                    Files.createDirectories(targetPath);
                }
                entry = inputStream.getNextEntry();
            }
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 文件单位转换
     *
     * @param size 文件大小
     * @return 带单位（B、KB、MB、GB）的文件大小
     */
    public static String convert(long size) {
        StringBuilder builder = new StringBuilder();
        // 单位为字节时
        if (size < 1024) {
            return builder.append(size).append("B").toString();
        } else {
            size = size / 1024;
        }

        // 单位为 KB 时
        if (size < 1024) {
            return builder.append(size).append("KB").toString();
        } else {
            size = size / 1024;
        }

        // 单位为 MB/GB 时的判断
        if (size < 1024) {
            size = size * 100;
            return builder
                .append(size / 100)
                .append(".")
                .append(size % 100)
                .append("MB")
                .toString();
        } else {
            size = size * 100 / 1024;
            return builder
                .append(size / 100)
                .append(".")
                .append(size % 100)
                .append("GB")
                .toString();
        }
    }

    /**
     * {@link InputStream} 转字节数组
     *
     * @param is {@link InputStream}
     * @return byte[]
     */
    public static byte[] getBytes(InputStream is) {
        ReadableByteChannel channel = Channels.newChannel(is);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            try {
                if (channel.read(buffer) == -1) break;
            } catch (IOException e) {
                log.error("IO Error: ", e);
            }
            buffer.flip();
        }
        return buffer.array();
    }

}
