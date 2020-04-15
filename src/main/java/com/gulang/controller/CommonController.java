package com.gulang.controller;

import com.gulang.model.RolePermission;
import com.gulang.service.RolePermissionService;
import com.gulang.service.UserRoleService;
import com.gulang.util.common.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonController {
    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RolePermissionService rolePermissionService;

    @Value("${common_auth}")
    public String commonAth;

    /**
     * 获取用户页面和操作权限
     * @param userId
     * @return
     */
    public  Map<String,Object> getUserPermissionList(long userId){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        List<Integer> roleIds = userRoleService.queryRoleIdByUserId(userId);
        List<RolePermission>  userAuths = rolePermissionService.roleHandlePermission(roleIds);

        StringBuffer roleHandleString = new StringBuffer();
        StringBuffer rolePageString   = new StringBuffer();

        roleHandleString.append(commonAth);

        for(RolePermission item:userAuths) {
            if(!item.getHandlePermission().equals("")) {
                roleHandleString.append(item.getHandlePermission()).append(",");
            }

            if(!item.getPagePermission().equals("")) {
                rolePageString.append(item.getPagePermission()).append(",");
            }
        }

        Object roleHandlePermission = ToolUtil.formateRolePermission(roleHandleString);
        Object rolePagePermission   = ToolUtil.formateRolePermission(rolePageString);

        resultMap.put("roleHandlePermission",roleHandlePermission);
        resultMap.put("rolePagePermission",rolePagePermission);

        return resultMap;
    }

}
