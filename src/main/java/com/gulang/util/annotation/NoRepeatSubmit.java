package com.gulang.util.annotation;

import java.lang.annotation.*;

/**
 * @describe: [防止表单重复提交的注解]
 * @author: liu zai chun
 * @date: 2020/4/29 10:54
 * @email: 1226740471@qq.com
 */
@Target({ElementType.METHOD, ElementType.TYPE})  // 注解为方法级别
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {
    /**
     * redis 保存token的有效期，放弃意外，导致没有及时删除key
     * @return
     */
    int tokenExp() default 30;
}
