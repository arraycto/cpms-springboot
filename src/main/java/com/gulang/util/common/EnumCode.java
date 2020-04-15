package com.gulang.util.common;


/*
 * 统一响应体中返回的状态码code和状态信息msg对应的枚举类
 */
public enum EnumCode {
    /*
     * 1000 (操作成功)
     * 1001（操作失败）
     * 1002（调用接口无权限）
     * 5000（token异常）
     * 5001（服务器未知错误）
     */

    /**
     * 操作成功返回的状态码
     */
    RESPONSE_SUCCESS(1000, "success"),

    /**
     * 操作失败返回的状态码
     */
    RESPONSE_FAIL(1001, "error"),

    /**
     * token异常返回状态码
     */
    RESPONSE_TOKEN_ERROR(5000, "token异常"),

    /**
     * 服务器异常返回状态码
     */
    RESPONSE_SERVICE_ERROR(5001, "服务器异常");

    /**
     * 状态码
     */
    private int code;
    /**
     * 返回信息
     */
    private String msg;

    EnumCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
