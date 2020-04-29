package com.gulang.core.shiro.filter;

import com.gulang.service.RolePermissionService;
import com.gulang.service.UserRoleService;
import com.gulang.util.common.JwtUtil;
import com.gulang.util.common.ToolUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * DESC:
 * 权限校验过滤器
 * @author gulang
 * @date 2019/7/20 17:56
 * @Email 1226740471@qq.com
 */

public class PermissionFilter extends AccessControlFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    private static final AntPathMatcher MATCHER = new AntPathMatcher(); //使用ant通配符匹配可以支持restful api风格的url
    @Override
    protected boolean isAccessAllowed(ServletRequest request,
                                      ServletResponse response, Object mappedValue) throws Exception {

        /*****************【获取JWT token信息，校验权限】********************/
        Map<String,Object> resultMap = new HashMap<String, Object>();

        HttpServletRequest httpRequest = ((HttpServletRequest)request);
        /**
         * 这里需要处理一下请求的URL路径，把它转成shiro addStringPermission 存储的URL格式，如：/user/add
         * 所以这里替换了一下，使用根目录开始的URI
         */
        String url = httpRequest.getRequestURI();//获取URI

        String basePath = httpRequest.getContextPath();//获取basePath
        if(null != url && url.startsWith(basePath)){
            url = url.replaceFirst(basePath, "");
        }


        //获取请求头部的token值，解析用户名，如果是 admin 则拥有所有权限
        String token      = httpRequest.getHeader("Token");
        Claims parseToken = JwtUtil.parseJwtToken(token);

        /**
         * 由于解析出来的parseToken 实质上是一个map集合   Map<String, Object> claims = new HashMap<String, Object>();
         * value是泛型Object
         * 1.当我们的value是字符串类型时，通过 (String)parseToken.get("username") 可以吧Object强制转换为String类型的
         * 2.当我们的value是Integer类型的 我们需要把它转为String类型，在通过Long.parseLong 把String转为 long类型的
         * (虽然map中取出的结果是 Object ，但其实质是 Integer ，故在将其强制转为 String 时会出现强制类型转换的异常,
         * 需要使用String.valueOf来转字符串）
         */

        String userId    = String.valueOf(parseToken.get("uid"));

        String roleHandlPermissionb = (String)parseToken.get("roleHandlePermission");

        List<String> currentUserHandlPermissionb = new ArrayList<String>();

        // 高效的数组转list集合
        Collections.addAll(currentUserHandlPermissionb, roleHandlPermissionb.split(","));

        if( Long.parseLong(userId) == 1 ) { // 超级管理员有所有的权限
            return Boolean.TRUE;
        }

        boolean isPermitted = matchPath(currentUserHandlPermissionb,url);  // 自定义方式进行权限判断，支持restful api
        if(isPermitted) {
            return Boolean.TRUE;
        }

        resultMap.put("code", 1002);
        resultMap.put("msg", "无操作权限");
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

    /**
     * des: 为了支持restful api 的url的权限判断，这里使用 AntPathMatcher 进行匹配判断是否有权限
     * @param authUrl   当前登录用户所有的权限url
     * @param url       当前请求的url
     * @return          boolean
     */
    private static boolean matchPath(List<String> authUrl,String url) {
        boolean matchResult = Boolean.FALSE;

        for (int i = 0; i < authUrl.size() ; i++) {
            matchResult = MATCHER.match(authUrl.get(i), url);
            if( matchResult ) {
                return matchResult;
            }
        }
        return matchResult;
    }
}
