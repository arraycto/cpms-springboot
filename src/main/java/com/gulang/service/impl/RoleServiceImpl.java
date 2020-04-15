package com.gulang.service.impl;

import com.gulang.dao.RoleDao;
import com.gulang.model.Role;
import com.gulang.model.RolePermission;
import com.gulang.service.RolePermissionService;
import com.gulang.service.RoleService;
import com.gulang.util.common.PageManager;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * DESC:
 *
 * @author gulang
 * @date 2019/9/18 19:58
 * @Email 1226740471@qq.com
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDao roleDao;

    @Autowired
    RolePermissionService rolePermissionService;

    @Override
    @Transactional(rollbackFor = Exception.class)  // 事务处理
    public boolean addRole(Role role,String pagePermission,String handlePermission) {

        try{
            roleDao.addRole(role);
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getRoleId());
            rolePermission.setPagePermission(pagePermission);
            rolePermission.setHandlePermission(handlePermission);
            rolePermissionService.addRolePermission(rolePermission);

            return true;
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键  捕获异常进行数据回滚
            return false;
        }
    }

    @Override
    public PageManager queryAllRole(int page, int pageSize) {
        int pageIndex = (page - 1)*pageSize;
        List<Role> roleList = roleDao.queryAllRole(pageIndex,pageSize);
        PageManager pageManager = new PageManager();
        int total = roleDao.roleTotal();
        pageManager.setCurrPage(page);
        pageManager.setPageSize(pageSize);
        pageManager.setTotalCount(total);
        pageManager.setTotalPage((total-1)/pageSize+1);
        pageManager.setLists(roleList);
        return pageManager;
    }

    @Override
    public Role queryByRoleId(Integer roleId) {
        return roleDao.queryByRoleId(roleId);
    }

    @Override
    public int selectRoleCount() {
        return roleDao.selectRoleCount();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)  // 事务处理
    public boolean deleteRole(Integer roleId){
         try{
             roleDao.deleteRole(roleId);
             // 返回空指针 抛出异常测试事务
//            String a = null;
//            a.indexOf('c');
             rolePermissionService.delRolePermission(roleId);
             return true;

         }catch (Exception e){
             TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键  捕获异常进行数据回滚
             return false;
         }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)  // 事务处理
    public boolean updateRole(Role role ,String pagePermission,String handlePermission){

        try{
            roleDao.updateRole(role);
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getRoleId());
            rolePermission.setPagePermission(pagePermission);
            rolePermission.setHandlePermission(handlePermission);
            rolePermissionService.updateRolePermission(rolePermission);
            return true;

        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键  捕获异常进行数据回滚
            return false;
        }

    }

    public int findRepeatRole(@Param("name")String name, @Param("roleId")Long roleId){
        return  roleDao.findRepeatRole(name,roleId);
    };

}
