package com.file.domain;

import java.io.Serializable;

/**
 * Excel 操作的相关参数对象
 *
 * @author LZH
 * @version 1.0.5
 * @since 2023-05-23
 */
public class ExcelArg<T> implements Serializable {

    private static final long serialVersionUID = 8892360328399170512L;

    /**
     * 私有构造函数
     */
    private ExcelArg() {}

    /**
     * 创建对象
     *
     * @param <T> 泛型
     * @return {@link ExcelArg}
     */
    public static <T> ExcelArg<T> build() {
        return new ExcelArg<>();
    }

}
