package com.gulang.util.aop;

import com.gulang.util.annotation.NoRepeatSubmit;
import com.gulang.util.common.EnumCode;
import com.gulang.util.common.ResponseResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


/**
 * @describe: [防止重复提交表单的aop切面]
 * @author: liu zai chun
 * @date: 2020/4/29 10:45
 * @email: 1226740471@qq.com
 */

@Aspect  // 声明该类为切面类
@Component
public class NoRepeatSubmitAspect {
    private static final Logger logger = LoggerFactory.getLogger(NoRepeatSubmitAspect.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Pointcut("@annotation(noRepeatSubmit)") // 定义切点，
    public void pointCut(NoRepeatSubmit noRepeatSubmit) {
    }

    @Around("pointCut(noRepeatSubmit)")
    public Object deAround(ProceedingJoinPoint joinPoint,NoRepeatSubmit noRepeatSubmit) throws Throwable {

        Object result = null;

        try {
            /*
             * 【注】
             *  其实这种方式设计防止表单重复提交，还是有缺陷的，如果在**高并发下**，第一个请求进来提交表单，执行完成删除key值，由于是在高并发下
             * 所以第二个请求有可能在第一个请求把key删除后才进入逻辑判断处理，此时发现没有key,会继续提交表单。
             * 解决高并发下的思路：
             * 1.首先前端页面自动生成一个随机的token，这个token字符串只有等后端有返回结果后，才去刷新token（快速点击按钮两次请求携带的token是一样的）
             *
             * 2.后端同样采用redis保存前端传来的token作为key(这里不同的地方在于，执行完后不去立即删除key,而是等待过期时间自动删除，这样的话就算高并发下
             * 也不会有问题
             * )
            */

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String key = request.getHeader("Token");  // token 作为key

            logger.info("***验证是否重复提交表单***");

            Boolean isRepeat = stringRedisTemplate.opsForValue().setIfAbsent("key", "",noRepeatSubmit.tokenExp(), TimeUnit.SECONDS);// 存在就返回false

            if(isRepeat) { // true 表示没有值，第一次提交

                result =  joinPoint.proceed(); // 调用被注解的方法

            }else{

               result = new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"重复提交表单",null);
            }

        } catch (Throwable e) {

            e.printStackTrace();

        }finally {
            // 删除redis 中的重复提交key
            stringRedisTemplate.delete("key");
        }

        return result;

    }
}
