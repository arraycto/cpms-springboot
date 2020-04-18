package com.gulang.exception;

import com.gulang.util.common.EnumCode;
import com.gulang.util.common.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @describe: [全局异常处理类]
 * @author: liu zai chun
 * @date: 2020/4/18 14:09
 * @email: 1226740471@qq.com
 */

@RestControllerAdvice  // 全局异常捕获注解，@RestControllerAdvice 返回json 类似于 @RestController
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)  // 指定捕获什么类型的异常，也可以自定义异常，然后在需要的地方 throw ，这里就会捕获到
    public ResponseResult exceptionHandler(Exception e) {
        logger.info("捕获到异常{}",e.getMessage());
        return new ResponseResult(EnumCode.RESPONSE_TOKEN_ERROR.getCode(),e.getMessage(),null);
    }

}
