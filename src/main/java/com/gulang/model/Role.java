package com.gulang.model;
import java.io.Serializable;
import java.util.List;

/**
 * DESC:
 *
 * @author gulang
 * @date 2019/7/16 17:17
 * @Email 1226740471@qq.com
 */

public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    private long roleId;
    private String roleName;
    private String roleDescript;
    private RolePermission rolePermission;

    public long getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getRoleDescript() {
        return roleDescript;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleDescript(String roleDescript) {
        this.roleDescript = roleDescript;
    }

    public RolePermission getRolePermission() {
        return rolePermission;
    }

    public void setRolePermission(RolePermission rolePermission) {
        this.rolePermission = rolePermission;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", roleDescript='" + roleDescript + '\'' +
                '}';
    }
}
