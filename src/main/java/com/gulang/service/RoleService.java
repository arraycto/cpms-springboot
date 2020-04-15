package com.gulang.service;

import com.gulang.model.Role;

import com.gulang.util.common.PageManager;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DESC:
 * @author gulang
 * @date 2019/7/16 17:08
 * @Email 1226740471@qq.com
 */
public interface RoleService {

    boolean addRole(Role role,String pagePermission,String handlePermission);

    PageManager queryAllRole(int page, int pageSize);

    /**
     * 传入的是两个以上的参数时，必须使用@Param (@Param("userName")String name)注解指明实体类字段对应的参数
     *  SELECT *  FROM user where user_name = #{userName} AND passwd = #{passwd}
     *  或者直接用map集合传参: key-value
     * */
    Role queryByRoleId(Integer roleId);

    int selectRoleCount();

    boolean deleteRole(Integer roleId);

    boolean updateRole(Role role ,String pagePermission,String handlePermission);

    int findRepeatRole(@Param("name")String name, @Param("roleId")Long roleId);
}
