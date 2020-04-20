package com.gulang.model;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.awt.print.Paper;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * DESC:
 *
 * @author gulang
 * @date 2019/7/16 17:17
 * @Email 1226740471@qq.com
 */

public class User implements Serializable {

    private static final long serialVersionUID = 7631697126193972999L;

    private long userId;
    private String userName;
    private  String roleIds;
    /**
     * 创建时间
     */
    // 通过全局配置，这里可以省略掉  @JsonFormat  注解
//    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     *  最后登录时间
     */
//    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;

    private  String  userPassword;
    private int  status;
    private List<Role> roles; //把关联属性集合映射到这个属性中

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getPassword() {
        return userPassword;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                ", lastLoginTime=" + lastLoginTime +
                ", userPassword='" + userPassword + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
