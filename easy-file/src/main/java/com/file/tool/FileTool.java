package com.file.tool;

import com.aspose.cells.*;
import com.aspose.pdf.Document;
import com.aspose.pdf.PageCollection;
import com.aspose.pdf.SaveFormat;
import com.aspose.pdf.devices.JpegDevice;
import com.aspose.pdf.devices.Resolution;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.ThumbnailGeneratingOptions;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 *
 * <ol>
 *     <li>{@link #copy(String, String)}: 文件复制</li>
 *     <li>{@link #save(byte[], String)}: 文件存储</li>
 *     <li>{@link #save(InputStream, String)}: 文件存储</li>
 *     <li>{@link #write(String, String)}: 文件写入</li>
 *     <li>{@link #write(byte[], String)}: 文件写入</li>
 *     <li>{@link #readString(String)}: 文件读取</li>
 *     <li>{@link #readStream(String)}: 文件读取</li>
 *     <li>{@link #readBytes(String)}: 文件读取</li>
 * </ol>
 * <hr/>
 * <ol>
 *     <li>{@link #split(File, int)}: 文件分片</li>
 *     <li>{@link #merge(File[], File)}: 文件合并</li>
 *     <li>{@link #zip(String, String)}: 单文件压缩</li>
 *     <li>{@link #zip(String[], String)}: 多文件压缩</li>
 *     <li>{@link #unzip(String, String)}: 文件解压</li>
 * </ol>
 * <hr/>
 * <ol>
 *     <li>{@link #convert(long)}: 文件单位转换</li>
 *     <li>{@link #toBytes(InputStream)}: {@link InputStream} 转字节数组</li>
 *     <li>{@link #toFileInputStream(InputStream)}: {@link InputStream} 转 {@link FileInputStream}</li>
 *     <li>{@link #prefix(File)}: 获取文件的前缀</li>
 *     <li>{@link #suffix(File)}: 获取文件的后缀</li>
 * </ol>
 * <hr/>
 * <ol>
 *     <li>{@link #convertImage(String, String, String, int, int)}: PDF 文件转图片</li>
 *     <li>{@link #convertPdf(InputStream, SaveFormat)}: PDF 文档转其它类型文件</li>
 *     <li>{@link #convertWord(InputStream, int)}: Word 文档转其他类型文件</li>
 *     <li>{@link #convertExcel(InputStream, int)}: Excel 文档转其他类型文件</li>
 * </ol>
 * <hr/>
 * <ol>
 *     <li>{@link #thumbnails(String, String, double, int, int)}: 生成图片缩略图</li>
 *     <li>{@link #thumbnails(String, double, int, int)}: 生成图片缩略图</li>
 *     <li>{@link #thumbnails(InputStream, String, double, int, int)}: 生成图片缩略图</li>
 *     <li>{@link #thumbnails(InputStream, double, int, int)}: 生成图片缩略图</li>
 * </ol>
 * <hr/>
 * <ol>
 *     <li>{@link #thumbnailsOfPdf(String, String, int, int)}: 生成 PDF 文档的缩略图</li>
 *     <li>{@link #thumbnailsOfPdf(String, int, int)}: 生成 PDF 文档的缩略图</li>
 *     <li>{@link #thumbnailsOfPdf(InputStream, String, int, int)}: 生成 PDF 文档的缩略图</li>
 *     <li>{@link #thumbnailsOfPdf(InputStream, int, int)}: 生成 PDF 文档的缩略图</li>
 * </ol>
 * <hr/>
 * <ol>
 *     <li>{@link #thumbnailsOfWord(String, String, double, double)}: 生成 Word 文档的缩略图</li>
 *     <li>{@link #thumbnailsOfWord(String, float)}: 生成 Word 文档的缩略图</li>
 *     <li>{@link #thumbnailsOfWord(InputStream, String, double, double)}: 生成 Word 文档的缩略图</li>
 *     <li>{@link #thumbnailsOfWord(InputStream, float)}: 生成 Word 文档的缩略图</li>
 * </ol>
 * <hr/>
 * <ol>
 *     <li>{@link #thumbnailsOfExcel(String, String, int)}: 生成 Excel 文档的缩略图</li>
 *     <li>{@link #thumbnailsOfExcel(String, int)}: 生成 Excel 文档的缩略图</li>
 *     <li>{@link #thumbnailsOfExcel(InputStream, String, int)}: 生成 Excel 文档的缩略图</li>
 *     <li>{@link #thumbnailsOfExcel(InputStream, int)}: 生成 Excel 文档的缩略图</li>
 * </ol>
 * <hr/>
 * <ol>
 *     <li>{@link #watermark(InputStream, String, InputStream, double, int, int, Positions, float)}: 为图片添加水印</li>
 *     <li>{@link #watermark(InputStream, InputStream, double, int, int, Positions, float)}: 为图片添加水印</li>
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
     * 文件存储
     *
     * @param bytes  字节数组
     * @param target 目标文件地址
     * @since 1.0.11
     */
    public static void save(byte[] bytes, String target) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {
            save(is, target);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 文件存储
     *
     * @param is     源文件
     * @param target 目标文件地址
     * @since 1.0.11
     */
    public static void save(InputStream is, String target) {
        try {
            Files.copy(is, Paths.get(target));
        } catch (IOException e) {
            log.error("IO Error: ", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.error("IO Error: ", e);
            }
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
     * @since 1.0.11
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
    public static byte[] toBytes(InputStream is) {
        ReadableByteChannel channel = Channels.newChannel(is);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            while (channel.read(buffer) != -1) {
                buffer.flip();
            }
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
        return buffer.array();
    }

    /**
     * {@link InputStream} 转 {@link FileInputStream}
     *
     * @param is {@link InputStream}
     * @return {@link FileInputStream}
     * @since 1.0.11
     */
    public static FileInputStream toFileInputStream(InputStream is) {
        try {
            Path temp = Files.createTempFile("resource-", "ext");
            Files.copy(is, temp, StandardCopyOption.REPLACE_EXISTING);
            return new FileInputStream(temp.toFile());
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
        return null;
    }

    /**
     * 获取文件的前缀
     *
     * <p><b style="color:red">如:</b> 深入理解Java虚拟机.1.pdf ===> 深入理解Java虚拟机.1</p>
     *
     * @param file 文件
     * @return 前缀名
     * @since 1.0.11
     */
    public static String prefix(File file) {
        return file.getName().replaceFirst("[.][^.]+$", "");
    }

    /**
     * 获取文件的后缀
     *
     * <p><b style="color:red">如:</b> 深入理解Java虚拟机.1.pdf ===> pdf</p>
     *
     * @param file 文件
     * @return 后缀名
     * @since 1.0.11
     */
    public static String suffix(File file) {
        Matcher matcher = Pattern.compile("\\.(\\w+)$").matcher(file.getName());
        return matcher.find() ? matcher.group(1) : "";
    }

    /**
     * PDF 文件转图片
     *
     * @param source      PDF 文件地址
     * @param target      生成图片的目标地址
     * @param suffix      图片的后缀, 如: png、jpeg、bmp 等
     * @param resolution  图片的分辨率
     * @param quality     图片的质量
     * @since 1.0.11
     */
    public static void convertImage(String source, String target, String suffix,
                                  int resolution, int quality) {
        Document doc = new Document(source);
        PageCollection pages = doc.getPages();
        int count = pages.size();

        JpegDevice jpegDevice = new JpegDevice(new Resolution(resolution), quality);

        // 文件前缀
        String prefix = prefix(new File(source));

        try {
            for (int i = 1; i <= count; i++) {
                File file = File.createTempFile("temp", suffix);
                try (FileOutputStream os = new FileOutputStream(file)) {
                    jpegDevice.process(pages.get_Item(i), os);
                }

                String path = target + File.separator + (prefix + "_" + i) + "." + suffix;
                ImageIO.write(ImageIO.read(file), suffix, new File(path));
            }
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * PDF 文档转其它类型文件
     *
     * @param is     PDF 文件
     * @param format 文件类型
     * @return {@link InputStream}
     * @since 1.0.11
     */
    public static InputStream convertPdf(InputStream is, SaveFormat format) {
        Document doc = new Document(is);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        doc.save(os, format);
        return new ByteArrayInputStream(os.toByteArray());
    }

    /**
     * Word 文档转其他类型文件
     *
     * @see com.aspose.words.SaveFormat
     * @param is     Word 文件地
     * @param format 文件类型
     * @return {@link InputStream}
     * @since 1.0.11
     */
    public static InputStream convertWord(InputStream is, int format) {
        try {
            com.aspose.words.Document doc = new com.aspose.words.Document(is);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            doc.save(os, format);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (Exception e) {
            log.error("IO Error: ", e);
        }
        return null;
    }

    /**
     * Excel 文档转其他类型文件
     *
     * @see com.aspose.cells.SaveFormat
     *
     * @param is     Excel 文件
     * @param format 文件类型
     * @return {@link InputStream}
     * @since 1.0.11
     */
    public static InputStream convertExcel(InputStream is, int format) {
        try {
            Workbook book = new Workbook(is);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            book.save(os, format);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (Exception e) {
            log.error("IO Error: ", e);
        }
        return null;
    }

    /**
     * 生成图片缩略图
     *
     * @param source 源图片地址
     * @param target 缩略图地址
     * @param scale  缩放比例
     * @param width  缩略图宽
     * @param height 缩略图高
     * @since 1.0.11
     */
    public static void thumbnails(String source, String target, double scale, int width, int height) {
        try (InputStream is = new FileInputStream(source)) {
            thumbnails(is, target, scale, width, height);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 生成图片缩略图
     *
     * @param source 源图片地址
     * @param scale  缩放比例
     * @param width  缩略图宽
     * @param height 缩略图高
     * @return {@link InputStream}
     * @since 1.0.11
     */
    public static InputStream thumbnails(String source, double scale, int width, int height) {
        try (InputStream is = new FileInputStream(source)) {
            return thumbnails(is, scale, width, height);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
        return null;
    }

    /**
     * 生成图片缩略图
     *
     * @param source 源图片
     * @param target 缩略图地址
     * @param scale  缩放比例
     * @param width  缩略图宽
     * @param height 缩略图高
     * @since 1.0.11
     */
    public static void thumbnails(InputStream source, String target, double scale, int width, int height) {
        try {
            Thumbnails.of(source).scale(scale).size(width, height).toFile(target);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 生成图片缩略图
     *
     * @param source 源图片
     * @param scale  缩放比例
     * @param width  缩略图宽
     * @param height 缩略图高
     * @return {@link InputStream}
     * @since 1.0.11
     */
    public static InputStream thumbnails(InputStream source, double scale, int width, int height) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            Thumbnails.of(source).scale(scale).size(width, height).toOutputStream(os);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
        return new ByteArrayInputStream(os.toByteArray());
    }

    /**
     * 生成 PDF 文档的缩略图
     *
     * @param source     源 PDF 文件地址
     * @param target     目标缩略图地址
     * @param resolution 缩略图分辨率
     * @param quality    缩略图质量
     * @since 1.0.11
     */
    public static void thumbnailsOfPdf(String source, String target, int resolution, int quality) {
        try (InputStream is = new FileInputStream(source)) {
            thumbnailsOfPdf(is, target, resolution, quality);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 生成 PDF 文档的缩略图
     *
     * @param source     源 PDF 文件地址
     * @param resolution 缩略图分辨率
     * @param quality    缩略图质量
     * @return {@link InputStream}
     * @since 1.0.11
     */
    public static InputStream thumbnailsOfPdf(String source, int resolution, int quality) {
        try (InputStream is = new FileInputStream(source)) {
            return thumbnailsOfPdf(is, resolution, quality);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
        return null;
    }

    /**
     * 生成 PDF 文档的缩略图
     *
     * @param is         源 PDF 文件
     * @param target     目标缩略图地址
     * @param resolution 缩略图分辨率
     * @param quality    缩略图质量
     * @since 1.0.11
     */
    public static void thumbnailsOfPdf(InputStream is, String target, int resolution, int quality) {
        Document doc = new Document(is);
        JpegDevice device = new JpegDevice(new Resolution(resolution), quality);
        device.process(doc.getPages().get_Item(1), target);
    }

    /**
     * 生成 PDF 文档的缩略图
     *
     * @param is         源 PDF 文件
     * @param resolution 缩略图分辨率
     * @param quality    缩略图质量
     * @return {@link InputStream}
     * @since 1.0.11
     */
    public static InputStream thumbnailsOfPdf(InputStream is, int resolution, int quality) {
        Document doc = new Document(is);
        JpegDevice device = new JpegDevice(new Resolution(resolution), quality);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        device.process(doc.getPages().get_Item(1), os);
        return new ByteArrayInputStream(os.toByteArray());
    }

    /**
     * 生成 Word 文档的缩略图
     *
     * @param source Word 文件地址
     * @param target 缩略图地址
     * @param width  缩略图宽
     * @param height 缩略图高
     * @since 1.0.11
     */
    public static void thumbnailsOfWord(String source, String target, double width, double height) {
        try (InputStream is = new FileInputStream(source)) {
            thumbnailsOfWord(is, target, width, height);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 生成 Word 文档的缩略图
     *
     * @param source Word 文件地址
     * @param scale  缩放比例
     * @return {@link InputStream}
     * @since 1.0.11
     */
    public static InputStream thumbnailsOfWord(String source, float scale) {
        try (InputStream is = new FileInputStream(source)) {
            return thumbnailsOfWord(is, scale);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
        return null;
    }

    /**
     * 生成 Word 文档的缩略图
     *
     * @param is     Word 文件
     * @param target 缩略图地址
     * @param width  缩略图宽
     * @param height 缩略图高
     * @since 1.0.11
     */
    public static void thumbnailsOfWord(InputStream is, String target, double width, double height) {
        try {
            com.aspose.words.Document doc = new com.aspose.words.Document(is);

            ThumbnailGeneratingOptions options = new ThumbnailGeneratingOptions();
            Dimension dimension = new Dimension();
            dimension.setSize(width, height);
            options.setThumbnailSize(dimension);

            doc.updateThumbnail(options);
            doc.save(target);
        } catch (Exception e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 生成 Word 文档的缩略图
     *
     * @param is     Word 文件
     * @param scale  缩放比例
     * @return {@link InputStream}
     * @since 1.0.11
     */
    public static InputStream thumbnailsOfWord(InputStream is, float scale) {
        try {
            com.aspose.words.Document doc = new com.aspose.words.Document(is);
            ImageSaveOptions options = new ImageSaveOptions(101);
            options.setScale(scale);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            doc.save(os, options);

            return new ByteArrayInputStream(os.toByteArray());
        } catch (Exception e) {
            log.error("IO Error: ", e);
        }
        return null;
    }

    /**
     * 生成 Excel 文档的缩略图
     *
     * @param source  Excel 文件地址
     * @param target  所略图地址
     * @param quality 缩略图质量
     * @since 1.0.11
     */
    public static void thumbnailsOfExcel(String source, String target, int quality) {
        try (InputStream is = new FileInputStream(source)) {
            thumbnailsOfExcel(is, target, quality);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 生成 Excel 文档的缩略图
     *
     * @param source  Excel 文件地址
     * @param quality 缩略图质量
     * @return {@link InputStream}
     * @since 1.0.11
     */
    public static InputStream thumbnailsOfExcel(String source, int quality) {
        try (InputStream is = new FileInputStream(source)) {
            return thumbnailsOfExcel(is, quality);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
        return null;
    }

    /**
     * 生成 Excel 文档的缩略图
     *
     * @param is      Excel 文件
     * @param target  所略图地址
     * @param quality 缩略图质量
     * @since 1.0.11
     */
    public static void thumbnailsOfExcel(InputStream is, String target, int quality) {
        try {
            // noinspection DuplicatedCode
            Workbook book = new Workbook(is);
            Worksheet sheet = book.getWorksheets().get(0);

            PageSetup setup = sheet.getPageSetup();
            setup.setOrientation(PageOrientationType.LANDSCAPE);

            ImageOrPrintOptions options = new ImageOrPrintOptions();
            options.setQuality(quality);

            SheetRender sr = new SheetRender(sheet, options);
            sr.toImage(0, target);
        } catch (Exception e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 生成 Excel 文档的缩略图
     *
     * @param is      Excel 文件
     * @param quality 缩略图质量
     * @return {@link InputStream}
     * @since 1.0.11
     */
    public static InputStream thumbnailsOfExcel(InputStream is, int quality) {
        try {
            // noinspection DuplicatedCode
            Workbook book = new Workbook(is);
            Worksheet sheet = book.getWorksheets().get(0);

            PageSetup setup = sheet.getPageSetup();
            setup.setOrientation(PageOrientationType.LANDSCAPE);

            ImageOrPrintOptions options = new ImageOrPrintOptions();
            options.setQuality(quality);

            SheetRender sr = new SheetRender(sheet, options);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            sr.toImage(0, os);

            return new ByteArrayInputStream(os.toByteArray());
        } catch (Exception e) {
            log.error("IO Error: ", e);
        }
        return null;
    }

    /**
     * 为图片添加水印
     *
     * @param source    源图片
     * @param target    目标图片
     * @param water     水印图片
     * @param scale     缩放比例
     * @param width     缩略图宽
     * @param height    缩略图高
     * @param positions 水印位置
     * @param opacity   水印透明度
     * @since 1.0.11
     */
    public static void watermark(InputStream source, String target, InputStream water,
                                 double scale, int width, int height,
                                 Positions positions, float opacity) {
        try {
            Thumbnails.of(source)
                .scale(scale)
                .size(width, height)
                .watermark(positions, ImageIO.read(water), opacity)
                .toFile(target);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
    }

    /**
     * 为图片添加水印
     *
     * @param source    源图片
     * @param water     水印图片
     * @param scale     缩放比例
     * @param width     缩略图宽
     * @param height    缩略图高
     * @param positions 水印位置
     * @param opacity   水印透明度
     * @return {@link InputStream}
     * @since 1.0.11
     */
    public static InputStream watermark(InputStream source, InputStream water,
                                        double scale, int width, int height,
                                        Positions positions, float opacity) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            Thumbnails.of(source)
                .scale(scale)
                .size(width, height)
                .watermark(positions, ImageIO.read(water), opacity)
                .toOutputStream(os);
        } catch (IOException e) {
            log.error("IO Error: ", e);
        }
        return new ByteArrayInputStream(os.toByteArray());
    }

}
