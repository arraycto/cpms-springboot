package com.gulang.service;

import com.gulang.model.UserRole;
import java.util.List;

/**
 * DESC:
 * @author gulang
 * @date 2019/7/16 17:08
 * @Email 1226740471@qq.com
 */

public interface UserRoleService {

    int addUserRole(UserRole userRole);
    int deleteUserRole(Integer userId);
    List<Integer> queryRoleIdByUserId(long userId);
}
