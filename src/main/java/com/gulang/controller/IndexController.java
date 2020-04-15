package com.gulang.controller;

import com.gulang.service.UserService;
import com.gulang.util.common.EnumCode;
import com.gulang.util.common.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public Object index(){

        return new ResponseResult(EnumCode.RESPONSE_SUCCESS.getCode(),"查询成功",null);
    }
}
