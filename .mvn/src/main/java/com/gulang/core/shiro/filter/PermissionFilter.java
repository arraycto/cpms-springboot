package com.gulang.core.shiro.filter;

import com.gulang.util.common.ToolUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * DESC:
 * 权限校验过滤器
 * @author gulang
 * @date 2019/7/20 17:56
 * @Email 1226740471@qq.com
 */
public class PermissionFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws Exception {

        Map<String,Object> resultMap = new HashMap<String, Object>();

        //先判断带参数的权限判断
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated()) {
            resultMap.put("code", 1001);
            resultMap.put("msg", "登录异常，重新登录");
            ToolUtil.outJson(response, resultMap);
            return Boolean.FALSE;
        }

        HttpServletRequest httpRequest = ((HttpServletRequest)request);
        /**
         * 这里需要处理一下请求的URL路径，把它转成shiro addStringPermission 存储的URL格式，如：/user/ajaxRequestUserList
         * 所以这里替换了一下，使用根目录开始的URI
         */
        String uri = httpRequest.getRequestURI();//获取URI

        System.out.println("当前请求的URL:"+uri);

        String basePath = httpRequest.getContextPath();//获取basePath
        if(null != uri && uri.startsWith(basePath)){
            uri = uri.replaceFirst(basePath, "");
        }

        //isPermitted 会触发 realm中的授权doGetAuthorizationInfo 方法
        if(subject.isPermitted(uri)){
            System.out.println("有权限");
            return Boolean.TRUE;
        }

        resultMap.put("code", 1002);
        resultMap.put("msg", "无权限访问");
        ToolUtil.outJson(response, resultMap);
        return Boolean.FALSE;
    }

    /*
    * 这个方法需要上面方法返回false时才会触发，如果上面方法返回true时，会继续执行下一个过滤器，如果没有下一个过滤器了
    * 会执行真正的操作（该干嘛干嘛）
    * 要是执行到这个方法了，一般直接返回false，进行无权限操作提示处理，终止整个URL请求验证过程
    * */
    @Override
    protected boolean onAccessDenied(ServletRequest request,ServletResponse response) {

        return Boolean.FALSE;
    }
}
