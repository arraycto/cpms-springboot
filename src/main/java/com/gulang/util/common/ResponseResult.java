package com.gulang.util.common;

import lombok.Data;


import java.io.Serializable;
import java.util.Map;

/*
 * des: 接口统一返回数据结果
 *
 */
@Data  // 定义响应体时，必须要有get set方法。否则返回结果时报错
public class ResponseResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private Object data;

    public ResponseResult(int resultCode, String message, Object resultMap) {
        code = resultCode;
        msg  = message;
        data = resultMap;
    }
}
