package com.gulang.controller;

import com.gulang.model.User;
import com.gulang.service.UserService;
import com.gulang.util.common.JwtUtil;
import com.gulang.util.common.ToolUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * DESC:
 *
 * @author gulang
 * @date 2019/7/15 17:29
 * @Email 1226740471@qq.com
 */
@RestController  // 由于是前后端分离，所以所有的方法都返回json数据
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/ulogin",method = RequestMethod.GET)

    public String submitLogin(String name,String password) {
        Map<String, String> resultMap = new HashMap<String, String>();

        // 1、获取Subject实例对象
        Subject currentUser = SecurityUtils.getSubject();
        // 3、将用户名和密码封装到UsernamePasswordToken, 由于没有使用shiro自动加密机制，所以这里需要自己加密在传参
        UsernamePasswordToken token = new UsernamePasswordToken(name, ToolUtil.md5UserPwd(password));

        try {
            //执行认证操作.
            currentUser.login(token);

            // 认证成功 创建JWT返回给前端
            User user = userService.queryByName(name);
            String jwtToken =  JwtUtil.createJWT(60*60*60*1000*2,user);
            System.out.println("jwt:"+jwtToken);

        }catch (AccountException ae) {

        }
        return ToolUtil.md5UserPwd("123456");
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }
}
