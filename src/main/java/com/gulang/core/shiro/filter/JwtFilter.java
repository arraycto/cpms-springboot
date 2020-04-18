package com.gulang.core.shiro.filter;

import com.gulang.util.common.JwtUtil;
import com.gulang.util.common.ToolUtil;

import io.jsonwebtoken.ExpiredJwtException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * DESC: 登录校验认证
 * @author gulang
 * @date 2019/7/20 17:56
 * @Email 1226740471@qq.com
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    /**
     *  des ：做登录验证处理，如果返回true,则进入到PermissionFilter 进行权限验证
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws UnauthorizedException
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws UnauthorizedException {

         Map<String,Object> resultMap = new HashMap<String, Object>();

         try {
             executeLogin(request,response);
             return Boolean.TRUE;

         } catch (Exception e) {
             resultMap.put("code", 5000);
             resultMap.put("msg", e.getMessage());
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

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String jwtToken = httpServletRequest.getHeader("Token");
        if(jwtToken == null ) {
            throw new Exception("请先登录");
        }

        try{

            boolean  check = JwtUtil.checkTokenExpire(jwtToken); // 这里进行token过期时间验证
            if(check) {
                logger.info("【即将过期，准备刷新token");

                String newJwt = JwtUtil.refreshToken(jwtToken);
                HttpServletResponse resp = (HttpServletResponse) response;

                resp.setHeader("refresh-token",newJwt); //把新的token设置到响应头，前端读取并刷新本地token
            }

        }catch (ExpiredJwtException expiredJwtException){

            throw new AuthenticationException("token过期，请重新登录");

        }catch (Exception e) {

            throw new AuthenticationException("token异常，请重新登录");
        }

        /*
         * 把JWT生成的jwtToken提交给realm进行登录验证，如果错误他会抛出异常并被捕获
         * 如果没有抛出异常则代表登入成功，返回true
        */
        /*
         *【注意:】我们这里使用shiro的过滤功能，不使用shiro的登入验证功能，所以用不到MyShiroRealm中的 doGetAuthenticationInfo方法
         *  所以在本系统中自定义的MyShiroRealm用不到
         */
        // getSubject(request, response).login(jwtToken); // 这里触发 realm的 doGetAuthenticationInfo方法

        return true;
    }

}
