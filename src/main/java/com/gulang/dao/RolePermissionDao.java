package com.gulang.dao;
import com.gulang.model.Role;
import com.gulang.model.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * DESC:
 * @author gulang
 * @date 2019/7/16 17:08
 * @Email 1226740471@qq.com
 */
@Mapper  //这里一定要写@Mapper 注解 否侧写其他的注解会报错扫描不到Dao层
public interface RolePermissionDao {

    int addRolePermission(RolePermission rolePermission);

    int delRolePermission(Integer roleId);

    int updateRolePermission(RolePermission rolePermission);

    List<RolePermission> roleHandlePermission(@Param("listRoleId") List<Integer> listRoleId);
}
