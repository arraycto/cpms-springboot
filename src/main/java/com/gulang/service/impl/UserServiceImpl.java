package com.gulang.service.impl;

import com.gulang.dao.UserDao;
import com.gulang.model.User;
import com.gulang.model.UserRole;
import com.gulang.service.UserRoleService;
import com.gulang.service.UserService;
import com.gulang.util.common.PageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DESC:
 *
 * @author gulang
 * @date 2019/7/17 16:55
 * @Email 1226740471@qq.com
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    UserRoleService userRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)  // 事务处理
    public boolean addUser(User user,String roleIds) {
        try{
            userDao.addUser(user);
            long uId = user.getUserId();
            String[] rIds = roleIds.split(",");

            for (int i = 0; i <rIds.length ; i++) {
                UserRole userRole = new UserRole();
                userRole.setUserId(uId);
                userRole.setRoleId(Long.parseLong(rIds[i]));
                userRoleService.addUserRole(userRole);
            }
            return true;
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键  捕获异常进行数据回滚
            return false;
        }
    }

    @Override
    public PageManager queryAllUser( int page, int pageSize) {
        int pageIndex = (page - 1)*pageSize;
        List<HashMap<String, Object>> userList = userDao.queryAllUser( pageIndex, pageSize);
        PageManager pageManager = new PageManager();
        int total = userDao.userTotal();
        pageManager.setCurrPage(page);
        pageManager.setPageSize(pageSize);
        pageManager.setTotalCount(total);
        pageManager.setTotalPage((total-1)/pageSize+1);
        pageManager.setLists(userList);
        return pageManager;
    }

    @Override
    public User queryByNamePwd(String name,String passwd) {
        return userDao.queryByNamePwd(name, passwd);
    }

    @Override
    public List<User> queryAssociation() {
        return null;
    }

    @Override
    public int selectUserCount() {
        return 0;
    }

    @Override
    public List<User> getUserByPage(Map<String, Object> map) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)  // 事务处理
    public  boolean updateUser(Integer userId,User user,String roleIds){

        try {
            userDao.updateUser(user);
            String[] rIds = roleIds.split(",");
            userRoleService.deleteUserRole(userId);
            for (int i = 0; i <rIds.length ; i++) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(Long.parseLong(rIds[i]));
                userRoleService.addUserRole(userRole);
            }
            return true;
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键  捕获异常进行数据回滚
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)  // 事务处理
    public boolean deleteUser(Integer userId){
        try{
            userDao.deleteUser(userId);
            userRoleService.deleteUserRole(userId);
            return true;
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//关键  捕获异常进行数据回滚
            return false;
        }

    }

    @Override
    public  User queryById(Long id) {
        return userDao.queryById(id);
    }

    @Override
    public  int updateLastLoginTime(Long userId){
        return  userDao.updateLastLoginTime(userId);
    }

    @Override
    public int findRepeatName(String name,Integer id){
        return userDao.findRepeatName(name,id);
    }
}
