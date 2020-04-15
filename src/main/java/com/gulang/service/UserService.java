package com.gulang.service;

import com.gulang.model.User;
import com.gulang.util.common.PageManager;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
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
    boolean addUser(User user,String roleIds);

     PageManager queryAllUser(int page, int pageSize);

    /**
     * 传入的是两个以上的参数时，必须使用@Param注解指明实体类字段对应的参数
     *  SELECT *  FROM user where user_name = #{userName} AND passwd = #{passwd}
     *  或者直接用map集合传参: key-value
     * */
    User queryByNamePwd(String name,String passwd);

    List<User> queryAssociation();

    int selectUserCount();

    List<User> getUserByPage(Map<String,Object> map);

    boolean updateUser(Integer userId,User user,String roleIds);

    boolean deleteUser(Integer userId);

     User queryById(Long id);
    int updateLastLoginTime(Long userId);
    int findRepeatName(String name,Integer id);
}
