package com.gulang.dao;

import com.gulang.model.Role;
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
public interface RoleDao {

    int addRole(Role role);

    List<Role> queryAllRole(int pageIndex,int pageSize);
    int roleTotal();
    Role queryByRoleId(@Param("roleId") Integer roleId);

    int selectRoleCount();

    int deleteRole(Integer roleId);

    int updateRole(Role role);

    int findRepeatRole(@Param("name")String name, @Param("roleId")Long roleId);
}
