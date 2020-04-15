package com.gulang.service.impl;

import com.gulang.dao.UserDao;
import com.gulang.model.User;
import com.gulang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public int addUser(User user) {
        return 0;
    }

    @Override
    public List<User> queryAllUser() {

        return userDao.queryAllUser();
    }

    @Override
    public User queryByName(String name) {
        return userDao.queryByName(name);
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
}
