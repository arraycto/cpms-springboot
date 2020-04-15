package com.gulang.dao;

import com.gulang.model.User;
import org.apache.ibatis.annotations.Mapper;

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

    int addUser(User user);

    List<User> queryAllUser();

    /**
     * 传入的是两个以上的参数时，必须使用@Param (@Param("userName")String name)注解指明实体类字段对应的参数
     *  SELECT *  FROM user where user_name = #{userName} AND passwd = #{passwd}
     *  或者直接用map集合传参: key-value
     * */
    User queryByName(String name);

    int selectUserCount();

    List<User> getUserByPage(Map<String,Object> map);
}
