package com.basic.enums;

/**
 * 响应状态码、 响应信息枚举类
 *
 * @author LZH
 * @version 1.0.0
 * @since 2023-05-02
 */
public enum StatusEnum {

    /**
     * 状态码 200
     * <p>操作成功</p>
     */
    CODE_200(200, "操作成功"),

    /**
     * 状态码 401
     * <p>未授权</p>
     */
    CODE_401(401, "未授权"),

    /**
     * 状态码 403
     * <p>访问受限，授权过期</p>
     */
    CODE_403(403, "访问受限，授权过期"),

    /**
     * 状态码 404
     * <p>资源未找到</p>
     */
    CODE_404(404, "资源未找到"),

    /**
     * 状态码 423
     * <p>您的账号已从其它设备登录, 请注意隐私保护</p>
     */
    code_423(423, "您的账号已从其它设备登录, 请注意隐私保护"),

    /**
     * 状态码 429
     * <P>操作过于频繁, 请稍后再试</P>
     */
    code_429(429, "操作过于频繁, 请稍后再试"),

    /**
     * 状态码 500
     * <p>系统内部错误</p>
     */
    CODE_500(500, "系统内部错误");

    private int code;

    private String msg;

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
     */
    public void setCode(int code) {
        this.code = code;
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

    /**
     * 全参构造函数
     *
     * @param code 响应状态码
     * @param msg  响应信息
     */
    StatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
