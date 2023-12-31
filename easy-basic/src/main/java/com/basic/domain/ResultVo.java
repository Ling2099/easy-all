package com.basic.domain;

import com.basic.enums.StatusEnum;

/**
 * 系统统一响应结果类封装
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023/05/02
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ResultVo<T> {

    /**
     * 响应状态码
     *
     * @mock 200
     * @since 1.0.0
     */
    private int code;

    /**
     * 响应信息
     *
     * @mock 操作成功
     * @since 1.0.0
     */
    private String msg;

    /**
     * 响应数据
     *
     * @mock {}
     * @since 1.0.0
     */
    private T data;

    /**
     * 私有化无参构造函数
     */
    private ResultVo() {}

    /**
     * 初始化当前对象
     */
    private volatile static ResultVo result = null;

    /**
     * 私有化全参构实例创建方法
     *
     * @param code 响应状态码
     * @param msg  响应信息
     * @param data 响应数据
     * @param <T>  泛型
     * @return {@link ResultVo}
     */
    private static <T> ResultVo<T> getInstance(int code, String msg, T data) {
        if (result == null) {
            synchronized (ResultVo.class) {
                if (result == null) {
                    result = new ResultVo<>();
                }
            }
        }
        return result.setCode(code).setMsg(msg).setData(data);
    }

    /**
     * 响应成功消息
     *
     * @param <T> 泛型
     * @return {@link ResultVo}
     */
    public static <T> ResultVo<T> ok() {
        return ok(StatusEnum.CODE_200.getMsg());
    }

    /**
     * 响应成功消息
     *
     * @param msg 响应信息
     * @param <T> 泛型
     * @return {@link ResultVo}
     */
    public static <T> ResultVo<T> ok(String msg) {
        return ok(msg, null);
    }

    /**
     * 响应成功消息
     *
     * @param data 响应数据
     * @param <T>  泛型
     * @return {@link ResultVo}
     */
    public static <T> ResultVo<T> ok(T data) {
        return ok(StatusEnum.CODE_200.getMsg(), data);
    }

    /**
     * 响应成功消息
     *
     * @param msg  响应信息
     * @param data 响应数据
     * @param <T>  泛型
     * @return {@link ResultVo}
     */
    public static <T> ResultVo<T> ok(String msg, T data) {
        return getInstance(StatusEnum.CODE_200.getCode(), msg, data);
    }

    /**
     * 响应失败信息
     *
     * @param <T> 泛型
     * @return {@link ResultVo}
     */
    public static <T> ResultVo<T> fail() {
        return fail(StatusEnum.CODE_500.getMsg());
    }

    /**
     * 响应失败信息
     *
     * @param msg 响应信息
     * @param <T> 泛型
     * @return {@link ResultVo}
     */
    public static <T> ResultVo<T> fail(String msg) {
        return fail(msg, null);
    }

    /**
     * 响应失败信息
     *
     * @param data 响应数据
     * @param <T>  泛型
     * @return {@link ResultVo}
     */
    public static <T> ResultVo<T> fail(T data) {
        return fail(StatusEnum.CODE_500, StatusEnum.CODE_500.getMsg(), data);
    }

    /**
     * 响应失败信息
     *
     * @param msg  响应信息
     * @param data 响应数据
     * @param <T>  泛型
     * @return {@link ResultVo}
     */
    public static <T> ResultVo<T> fail(String msg, T data) {
        return fail(StatusEnum.CODE_500, msg, data);
    }

    /**
     * 响应失败信息
     *
     * @param status {@link StatusEnum} 响应状态码
     * @param <T>    泛型
     * @return {@link ResultVo}
     */
    public static <T> ResultVo<T> fail(StatusEnum status) {
        return fail(status, status.getMsg());
    }

    /**
     * 响应失败信息
     *
     * @param status {@link StatusEnum} 响应状态码
     * @param msg    响应信息
     * @param <T>    泛型
     * @return {@link ResultVo}
     */
    public static <T> ResultVo<T> fail(StatusEnum status, String msg) {
        return fail(status, msg, null);
    }

    /**
     * 响应失败信息
     *
     * @param status {@link StatusEnum} 响应状态码
     * @param msg    响应信息
     * @param data   响应数据
     * @param <T>    泛型
     * @return {@link ResultVo}
     */
    public static <T> ResultVo<T> fail(StatusEnum status, String msg, T data) {
        return getInstance(status.getCode(), msg, data);
    }

    /**
     * getter function
     *
     * @return int
     */
    public int getCode() {
        return code;
    }

    /**
     * setter function
     *
     * @param code {@link #code}
     * @return {@link ResultVo}
     */
    public ResultVo setCode(int code) {
        this.code = code;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link String}
     */
    public String getMsg() {
        return msg;
    }

    /**
     * setter function
     *
     * @param msg {@link #msg}
     * @return {@link ResultVo}
     */
    public ResultVo setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * getter function
     *
     * @return {@link #data}
     */
    public T getData() {
        return data;
    }

    /**
     * setter function
     *
     * @param data {@link #data}
     * @return {@link ResultVo}
     */
    public ResultVo setData(T data) {
        this.data = data;
        return this;
    }
}
