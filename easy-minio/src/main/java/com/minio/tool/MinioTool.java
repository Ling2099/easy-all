package com.minio.tool;

import io.minio.*;
import io.minio.errors.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MinIO 工具类
 *
 * <ol>
 *     <li>{@link #create(String)}: 创建桶</li>
 *     <li>{@link #upload(InputStream, long, String, String)}: 对象上传</li>
 *     <li>{@link #remove(String, String)}: 对象删除</li>
 *     <li>{@link #getObj(String, String)}: 对象获取</li>
 * </ol>
 *
 * @author LZH
 * @version 1.0.8
 * @since 2023/07/14
 */
public class MinioTool {

    /**
     * 日志记录: {@link Logger}
     */
    private static final Logger log = Logger.getLogger("com.minio.tool.MinioTool");

    private final MinioClient minio;

    public MinioTool(MinioClient minio) {
        this.minio = minio;
    }

    /**
     * 创建桶
     *
     * @param bucketName 桶名称
     */
    public void create(String bucketName) {
        boolean hasCreate = false;
        try {
            hasCreate = minio.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (ErrorResponseException | InsufficientDataException
                | InternalException | InvalidKeyException
                | InvalidResponseException | IOException
                | NoSuchAlgorithmException | ServerException
                | XmlParserException e) {
            log.log(Level.SEVERE, "BucketExists Exception: ", e);
        }

        if (hasCreate) {
            return;
        }

        try {
            minio.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } catch (ErrorResponseException | InsufficientDataException
                | InternalException | InvalidKeyException
                | InvalidResponseException | IOException
                | NoSuchAlgorithmException | ServerException
                | XmlParserException e) {
            log.log(Level.SEVERE, "MakeBucket Exception: ", e);
        }
    }

    /**
     * 对象上传
     *
     * @param is         文件输入流 {@link InputStream}
     * @param size       对象大小
     * @param bucketName 桶名称
     * @param path       文件路径
     */
    public void upload(InputStream is, long size, String bucketName, String path) {
        try {
            minio.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .stream(is, size, PutObjectArgs.MIN_MULTIPART_SIZE)
                    .build()
            );
        } catch (ErrorResponseException | InsufficientDataException
                | InternalException | InvalidKeyException
                | InvalidResponseException | IOException
                | NoSuchAlgorithmException
                | ServerException | XmlParserException e) {
            log.log(Level.SEVERE, "Upload Exception: ", e);
        }
    }

    /**
     * 对象删除
     *
     * @param bucketName 桶名称
     * @param path       文件路径
     */
    public void remove(String bucketName, String path) {
        try {
            minio.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(path)
                    .build()
            );
        } catch (ErrorResponseException | InsufficientDataException
                | InternalException | InvalidResponseException
                | InvalidKeyException | IOException
                | NoSuchAlgorithmException | ServerException
                | XmlParserException e) {
            log.log(Level.SEVERE, "Remove Exception: ", e);
        }
    }

    /**
     * 对象获取
     *
     * @param bucketName 桶名称
     * @param path       文件路径
     * @return {@link InputStream}
     */
    public InputStream getObj(String bucketName, String path) {
        try {
            return minio.getObject(GetObjectArgs.builder().bucket(bucketName).object(path).build());
        } catch (ErrorResponseException | InsufficientDataException
                | InternalException | InvalidKeyException
                | InvalidResponseException | IOException
                | NoSuchAlgorithmException | ServerException
                | XmlParserException e) {
            log.log(Level.SEVERE, "Download Exception: ", e);
        }
        return null;
    }

}
