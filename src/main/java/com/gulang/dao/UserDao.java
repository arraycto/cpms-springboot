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
    /*
     * （传递实体类，不使用 @Param 注解）
     * 同时 mapper.xml 文件也不需要使用 parameterType指定参数类型，
     * Mybatis会根据实体类(entity)的各个属性类型自动识别并匹配;mapper.xml中的变量必须与实体类的属性名对应
     * 【注：使用@Param，xml中只能用#{}】
     */
    int addUser(User user);

    int userTotal();
    List<HashMap<String, Object>> queryAllUser(@Param("pageIndex")int pageIndex,@Param("pageSize")int pageSize);

    /*
     * 1.（传递一个参数，可以不使用 @Param 注解）
     *  mapper.xml 文件会默认sql中的变量为方法的第一个参数，变量名于方法参数一致
     *
     * 2.（传递两个以上的参数，使用 @Param 注解,一般传递4个以上建议使用Map集合来传递）
     *  method(@Param("userName")String name,@Param("userPassword")String userPassword)；同时，mapper.xml中的变量必须
     *  要和@Param("userName") 中的一致
     *  eg:
     *  SELECT *  FROM user where user_name = #{userName} AND passwd = #{userPassword}
     *
     */
    User queryByNamePwd(@Param("name")String name, @Param("passwd")String passwd);

    /*
     * (传递多个参数，不使用 @Param 注解)
     * 这个时候可以使用Map集合类，key对应mapper.xml文件中的变量。如果key对应的值是一个实体类（javabean）.则在xml文件中必须指明是哪个
     * 属性,同时xml文件添加 parameterType="java.util.Map"
     * eg:
     * dao层： List searchUser(Map<String,Object>);
     *
     * service调用dao层：
     *
     *    UserInfo userInfo = new UserInfo();
     *    Pagination page = new Pagination();
     *    Map<String,Object> map = new HashMap<>;
     *    map.put("userInfo",userInfo);
     *    pam.put("page",page);
     *    userService.searchUser(map);
     *  xml:
     *  	<select id="searchUser" parameterType="java.util.Map" resultType="UserInfo">
     * 		   select *
     * 		   from t_userinfo user
     * 		   where 1 =1
     * 		   and user.uname like '%${userInfo.uname}$%'
     * 		   limit ${page.pagenum * page.limitnum}, #{page.limitnum}
     * 	   </select>
     *
     */
    List<User> getUserByPage(Map<String,Object> map);

    int updateUser(User user);
    int deleteUser(Integer userId);
    int updateLastLoginTime(Long userId);
    User queryById(Long id);

    int findRepeatName(@Param("name")String name, @Param("id")Integer id);
}
