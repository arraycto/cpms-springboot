package com.gulang.service.impl;

import com.gulang.dao.UserRoleDao;
import com.gulang.model.UserRole;
import com.gulang.service.UserRoleService;
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
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    UserRoleDao userRoleDao;

    @Override
    public int addUserRole(UserRole userRole) {
        return userRoleDao.addUserRole(userRole);
    }

    @Override
    public  int deleteUserRole(Integer userId){

        return  userRoleDao.deleteUserRole(userId);
   }

    @Override
    public List<Integer> queryRoleIdByUserId(long userId){
        return userRoleDao.queryRoleIdByUserId(userId);
    }
}
