package com.gulang.dao;
import com.gulang.model.Role;
import com.gulang.model.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * DESC:
 * @author gulang
 * @date 2019/7/16 17:08
 * @Email 1226740471@qq.com
 */
@Mapper  //这里一定要写@Mapper 注解 否侧写其他的注解会报错扫描不到Dao层
public interface RolePermissionDao {

    int addRolePermission(RolePermission rolePermission);

    int delRolePermission(Integer roleId);

    int updateRolePermission(RolePermission rolePermission);

    /**
     * 在mybatis的sql语句中使用IN查询多条数据时，如果把roleIds转成字符串形式通过使用 IN（${roleIds}）时 则查询出来只有一条数据
     * 一般我们只需要传list集合类型然后在xml文件通过foreach 形式查询
     * <select id="roleHandlePermission" resultType="string" parameterType="List">
     *         SELECT
     *         handle_permission
     *         FROM cpms_role_permission  WHERE role_id IN
     *    <foreach collection="listRoleId" item="roleId" index="index" open="(" separator="," close=")">
     *             #{roleId}
     *    </foreach>
     * </select>
     * 注：在Dao层传参的时候需要指明 collection="listRoleId"
     */
    List<RolePermission> roleHandlePermission(@Param("listRoleId") List<Integer> listRoleId);
}
