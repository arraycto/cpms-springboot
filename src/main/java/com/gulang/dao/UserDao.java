package com.gulang.dao;

import com.gulang.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DESC:
 * @author gulang
 * @date 2019/7/16 17:08
 * @Email 1226740471@qq.com
 */
@Mapper  //这里一定要写@Mapper 注解 否侧写其他的注解会报错扫描不到Dao层
public interface UserDao {

    /***********dao层方法的参数于mapper.xml的sql语句变量的关系***********/

    int addUser(User user);

    int userTotal();
    List<HashMap<String, Object>> queryAllUser(@Param("pageIndex")int pageIndex,@Param("pageSize")int pageSize);


    User queryByNamePwd(@Param("name")String name, @Param("passwd")String passwd);

    List<User> getUserByPage(Map<String,Object> map);

    int updateUser(User user);
    int deleteUser(Integer userId);
    int updateLastLoginTime(Long userId);
    User queryById(Long id);

    int findRepeatName(@Param("name")String name, @Param("id")Integer id);
}
