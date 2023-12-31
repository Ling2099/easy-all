package com.basic.exception;

/**
 * 自定义基础异常
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023/05/02
 */
public class BaseException extends RuntimeException {

    /**
     * 自定义异常提示
     */
    private String msg;

    /**
     * 无参构造函数
     */
    public BaseException() {
        super();
    }

    /**
     * 带参构造函数
     *
     * @param msg 错误提示
     */
    public BaseException(String msg) {
        super();
        this.msg = msg;
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
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
