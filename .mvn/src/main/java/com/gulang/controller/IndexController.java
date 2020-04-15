package com.gulang.controller;

import com.gulang.model.User;
import com.gulang.service.UserService;
import com.gulang.util.common.JwtUtil;
import com.gulang.util.common.ToolUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * DESC:
 *  站点首页
 * @author gulang
 * @date 2019/7/15 17:29
 * @Email 1226740471@qq.com
 */
@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public Object index(HttpServletRequest request) {

//        //获取请求头部的token值
        String token    = request.getHeader("Token");
        Claims parseToken = JwtUtil.parseJWT(token);
        Object uname =  parseToken.get("username");
        System.out.println("token解析："+uname);
//        User user = userService.queryByName(parseToken.get("username"));
        return ToolUtil.commonResult(1000,"查询成功","2323");
    }

    @RequestMapping(value = "/shiroTest",method = RequestMethod.GET)
    //@PathVariable 获取restful 风格的url参数
    public String shiroTest() {

//         return "注入组件方式获取值：name-"+boyComponent.getName()+ " age-"+boyComponent.getAge();
        return  "shiro权限测试";
    }
}
