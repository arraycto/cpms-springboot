package com.gulang.model;
import java.io.Serializable;

/**
 * DESC:
 *
 * @author gulang
 * @date 2019/7/16 17:17
 * @Email 1226740471@qq.com
 */

public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;
    private long roleId;
    private String pagePermission;
    private String handlePermission;


    public long getRoleId() {
        return roleId;
    }

    public String getPagePermission() {
        return pagePermission;
    }

    public String getHandlePermission() {
        return handlePermission;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public void setPagePermission(String pagePermission) {
        this.pagePermission = pagePermission;
    }

    public void setHandlePermission(String handlePermission) {
        this.handlePermission = handlePermission;
    }

    @Override
    public String toString() {
        return "RolePermission{" +
                "roleId=" + roleId +
                ", pagePermission='" + pagePermission + '\'' +
                ", handlePermission='" + handlePermission + '\'' +
                '}';
    }
}
