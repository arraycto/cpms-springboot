package com.gulang.controller;

import com.gulang.model.User;
import com.gulang.service.RolePermissionService;
import com.gulang.service.UserRoleService;
import com.gulang.service.UserService;
import com.gulang.util.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * DESC:
 *  用户页面
 * @author gulang
 * @date 2019/7/15 17:29
 * @Email 1226740471@qq.com
 */
@RestController
@RequestMapping("/user")
public class UserController extends CommonController {
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RolePermissionService rolePermissionService;


    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public Object userInfo(HttpServletRequest request) {
        /*
         * 由于使用jwt保存用户信息，为了避免修改了用户信息，不重新登录的话不会立即生效，刷新页面会调用这个接口，
         * 所以我们在此接口中重新获取用户信息并生成token值。这样的话，页面刷新就可以重新生成最新的token值，
         * 避免了重新登录来刷新token
        */
        Map<String,Object> resultMap = new HashMap<String, Object>();
        Map<String,Object> tokenMap = new HashMap<String, Object>();

        /*
         * 获取请求头部的token值
         */
        String token    = request.getHeader("Token");
        Map<String,String> parseToken = ToolUtil.getUserInfoByToken(token);

        User userModel = userService.queryById(Long.parseLong(parseToken.get("userId")));

        tokenMap.put("uid",parseToken.get("userId"));
        tokenMap.put("username",userModel.getUserName());

        Map<String, Object>  authMap = getUserPermissionList(Long.parseLong(parseToken.get("userId")));


        tokenMap.put("roleHandlePermission", authMap.get("roleHandlePermission"));

        String  jwtToken = JwtUtil.createJWT(tokenMap);

        resultMap.put("roleHandlePermission", authMap.get("roleHandlePermission"));
        resultMap.put("rolePagePermission", authMap.get("rolePagePermission"));
        resultMap.put("userName",userModel.getUserName());
        resultMap.put("token",jwtToken);

        return new ResponseResult(EnumCode.RESPONSE_SUCCESS.getCode(),"刷新成功",resultMap);
    }


    @RequestMapping(value = "/allUser",method = RequestMethod.GET)
    public Object allUser(@RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10")int pageSize) {

        List<Object> userList = new ArrayList<Object>();

        PageManager pageData = userService.queryAllUser(page,pageSize);

        return new ResponseResult(EnumCode.RESPONSE_SUCCESS.getCode(),"查询成功",  pageData);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object addUser(String name,String password,String roleIds) {

        int count = userService.findRepeatName(name,null);
        if(count > 0) {
            return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"用户名已存在",null);
        }

        User user = new User();
        user.setCreateTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        user.setUserName(name);
        user.setPassword(ToolUtil.md5UserPwd(password));

        // 这里需要事务回滚处理
        boolean res = userService.addUser(user,roleIds);

        if(res) {

            return new ResponseResult(EnumCode.RESPONSE_SUCCESS.getCode(),"添加成功",null);
        }

        return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"添加失败",null);
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public Object editUser(Integer userId,String name,String roleIds) {
        if(userId == 1) {
            return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"无权限编辑超级用户",null);
        }

        int count = userService.findRepeatName(name,userId);

        if(count > 0) {
            return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"用户名已存在",null);
        }

        User user = new User();
        user.setUserId(userId);
        user.setUserName(name);

        // 这里需要事务回滚处理
        boolean res = userService.updateUser(userId,user,roleIds);
        if(res) {

            return new ResponseResult(EnumCode.RESPONSE_SUCCESS.getCode(),"更新成功",null);
        }

        return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"更新失败",null);

    }

    @GetMapping("/delete")
    public Object deleteUser(Integer userId){
        if(userId == 1) {

            return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"无权限删除超级用户",null);
        }

        boolean res = userService.deleteUser(userId);

        if(res) {

            return new ResponseResult(EnumCode.RESPONSE_SUCCESS.getCode(),"删除成功",null);
        }

        return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"删除失败",null);
    }
}
