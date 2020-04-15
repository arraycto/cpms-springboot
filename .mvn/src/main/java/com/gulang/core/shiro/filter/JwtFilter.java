package com.gulang.core.shiro.filter;

import com.gulang.util.common.JwtUtil;
import com.gulang.util.common.ToolUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * DESC:
 *  拦截是否登陆
 * @author gulang
 * @date 2019/7/20 17:56
 * @Email 1226740471@qq.com
 */
public class JwtFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws Exception {

            Map<String,Object> resultMap = new HashMap<String, Object>();

            //获取请求头部的token值
             String token    = ((HttpServletRequest) request).getHeader("Token");

             System.out.println("token验证中...");
             try{
                 Claims  parseToken = JwtUtil.parseJWT(token);
                 return Boolean.TRUE;
             }catch (Exception e) {

                 resultMap.put("code", "1001");
                 resultMap.put("msg", "登录异常，重新登录");
                 ToolUtil.outJson(response, resultMap);
                 return Boolean.FALSE;
             }
    }

    /*
    * 这个方法需要上面方法返回false时才会触发，如果上面方法返回true时，会继续执行下一个过滤器，如果没有下一个过滤器了
    * 会执行真正的操作（该干嘛干嘛）
    * 要是执行到这个方法了，一般直接返回false，进行无权限操作提示处理，终止整个URL请求验证过程
    * */
    @Override
    protected boolean onAccessDenied(ServletRequest request,
                                     ServletResponse response) {

        return Boolean.FALSE;
    }
}
