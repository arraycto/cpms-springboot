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

public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;
    private long userId;
    private long roleId;


    public long getUserId() {
        return userId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                ", userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }
}
