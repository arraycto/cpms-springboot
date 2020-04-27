package com.gulang.controller;

import com.gulang.model.User;
import com.gulang.service.RolePermissionService;
import com.gulang.service.UserRoleService;
import com.gulang.service.UserService;
import com.gulang.util.common.EnumCode;
import com.gulang.util.common.JwtUtil;
import com.gulang.util.common.ResponseResult;
import com.gulang.util.common.ToolUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * DESC: 登录操作
 *
 * @author gulang
 * @date 2019/7/15 17:29
 * @Email 1226740471@qq.com
 */
@RestController  // 由于是前后端分离，所以所有的方法都返回json数据
@RequestMapping("/login")
public class LoginController extends CommonController
{

    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RolePermissionService rolePermissionService;

    @ApiOperation(value="登入后台api", notes="post方式传递提交账号和密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", paramType = "query", value = "账号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", paramType = "query", value = "密码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/ulogin",method = RequestMethod.POST)
    public Object submitLogin( String username, String password) {

        User user = userService.queryByNamePwd(username,ToolUtil.md5UserPwd(password));
        if( user == null ) {

            return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"用户名/密码不正确",null);

        }else {

            //更新最后登录时间
            userService.updateLastLoginTime(user.getUserId());

            Map<String,Object> resultMap = new HashMap<String, Object>();

            Map<String,Object> tokenMap = new HashMap<String, Object>();

            tokenMap.put("uid",user.getUserId());
            tokenMap.put("username", user.getUserName());
            /*
             * 登录成功后，生成token（token由用户id、用户名、操作权限组成）
             * 并返回结果：
             * token 页面权限数据 操作权限数据 用户名
             **/
            Map<String, Object>  authMap = getUserPermissionList(user.getUserId());

            tokenMap.put("roleHandlePermission", authMap.get("roleHandlePermission"));

            String  jwtToken = JwtUtil.createJWT(tokenMap);
            resultMap.put("roleHandlePermission", authMap.get("roleHandlePermission"));
            resultMap.put("rolePagePermission", authMap.get("rolePagePermission"));
            resultMap.put("userName",user.getUserName());
            resultMap.put("token",jwtToken);
            return new ResponseResult(EnumCode.RESPONSE_SUCCESS.getCode(),"登录成功",resultMap);
        }
    }

    @ApiOperation(value="登出后台api", notes="目前暂时还没用到")
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public Object logout() {

        return new ResponseResult(EnumCode.RESPONSE_SUCCESS.getCode(),"登出成功",null);
    }
}
