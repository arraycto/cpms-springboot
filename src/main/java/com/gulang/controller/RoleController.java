package com.gulang.controller;

import com.gulang.model.Role;
import com.gulang.service.RolePermissionService;
import com.gulang.service.RoleService;
import com.gulang.util.common.EnumCode;
import com.gulang.util.common.PageManager;
import com.gulang.util.common.ResponseResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * DESC:
 *  角色页面
 * @author gulang
 * @date 2019/7/15 17:29
 * @Email 1226740471@qq.com
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    RoleService roleService;

    @Autowired
    RolePermissionService rolePermissionService;

    @RequestMapping(value = "/allRole",method = RequestMethod.GET)
    public Object queryAllRole(@RequestParam(defaultValue = "1")int page,@RequestParam(defaultValue = "10")int pageSize) {

        PageManager pageData  = roleService.queryAllRole(page,pageSize);

        return new ResponseResult(EnumCode.RESPONSE_SUCCESS.getCode(),"查询成功",pageData);
    }

    @PostMapping("/add")
    public  Object addRole(Role roleInfo,String pagePermission,String handlePermission){
        int count = roleService.findRepeatRole(roleInfo.getRoleName(),null);

        if(count > 0) {
            return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"角色名已存在",null);
        }
        // 需要做事务回滚处理
        boolean res = roleService.addRole(roleInfo,pagePermission,handlePermission);

        if(res) {
            return new ResponseResult(EnumCode.RESPONSE_SUCCESS.getCode(),"添加成功",null);
        }
        return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"添加失败",null);

    }

    @GetMapping("/delete")
    public  Object delRole(Integer roleId){
        if(roleId == 1) {
            return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"无权限删除超级角色",null);
        }
        // 需要做事务回滚处理
        boolean res = roleService.deleteRole(roleId);

        if(res) {

            return new ResponseResult(EnumCode.RESPONSE_SUCCESS.getCode(),"删除成功",null);
        }

        return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"删除失败",null);
    }

    @PostMapping("/edit")
    public  Object editRole(Role roleInfo,String pagePermission,String handlePermission){

        if(roleInfo.getRoleId() == 1) {
            return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"无权限编辑超级角色",null);
        }

        int count = roleService.findRepeatRole(roleInfo.getRoleName(),roleInfo.getRoleId());

        if(count > 0) {
            return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"角色名已存在",null);
        }

        // 需要做事务回滚处理
        boolean res = roleService.updateRole(roleInfo,pagePermission,handlePermission);

        if(res) {

            return new ResponseResult(EnumCode.RESPONSE_SUCCESS.getCode(),"更新成功",null);
        }

        return new ResponseResult(EnumCode.RESPONSE_FAIL.getCode(),"更新失败",null);
    }
}
