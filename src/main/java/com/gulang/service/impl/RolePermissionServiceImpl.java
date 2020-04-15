package com.gulang.service.impl;

import com.gulang.dao.RolePermissionDao;
import com.gulang.model.RolePermission;
import com.gulang.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * DESC:
 *
 * @author gulang
 * @date 2019/7/17 16:55
 * @Email 1226740471@qq.com
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    @Autowired
    RolePermissionDao rolePermissionDao;

    @Override
    public int addRolePermission(RolePermission rolePermission) {
        return  rolePermissionDao.addRolePermission(rolePermission);
    }

    @Override
    public int delRolePermission(Integer roleId) {
        return  rolePermissionDao.delRolePermission(roleId);
    }

    @Override
    public  int  updateRolePermission(RolePermission rolePermission){

        return rolePermissionDao.updateRolePermission(rolePermission);
    }

    @Override
    public List<RolePermission> roleHandlePermission(List<Integer> listRoleId){
        return rolePermissionDao.roleHandlePermission(listRoleId);
    }
}
