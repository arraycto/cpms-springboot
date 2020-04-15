package com.gulang.service;

import com.gulang.model.RolePermission;

import java.util.List;
import java.util.Map;


/**
 * DESC:
 * @author gulang
 * @date 2019/7/17 16:53
 * @Email 1226740471@qq.com
 */
public interface RolePermissionService {
    int addRolePermission(RolePermission rolePermission);

    int delRolePermission(Integer roleId);

    int updateRolePermission(RolePermission rolePermission);

    List<RolePermission> roleHandlePermission(List<Integer> listRoleId);
}
