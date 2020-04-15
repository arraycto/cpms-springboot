package com.gulang.service;

import com.gulang.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * DESC:
 *
 * @author gulang
 * @date 2019/7/17 16:53
 * @Email 1226740471@qq.com
 */
public interface UserService {
    int addUser(User user);

    List<User> queryAllUser();

    /**
     * 传入的是两个以上的参数时，必须使用@Param注解指明实体类字段对应的参数
     *  SELECT *  FROM user where user_name = #{userName} AND passwd = #{passwd}
     *  或者直接用map集合传参: key-value
     * */
    User queryByName(String name);

    List<User> queryAssociation();

    int selectUserCount();

    List<User> getUserByPage(Map<String,Object> map);
}
