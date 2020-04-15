package com.gulang.dao;

import com.gulang.model.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * DESC:
 * @author gulang
 * @date 2019/7/16 17:08
 * @Email 1226740471@qq.com
 */
@Mapper  //这里一定要写@Mapper 注解 否侧写其他的注解会报错扫描不到Dao层
public interface UserRoleDao {

    int addUserRole(UserRole userRole);

    int deleteUserRole(Integer userId);

    List<Integer> queryRoleIdByUserId(long userId);
}
