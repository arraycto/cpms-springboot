package com.gulang.model;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.awt.print.Paper;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * DESC:
 *
 * @author gulang
 * @date 2019/7/16 17:17
 * @Email 1226740471@qq.com
 */

/***
 * 我们都知道序列化和反序列化是怎么回事，简单来讲，Java对象只存在于JVM中，当JVM退出时，对象也会被销毁。
 * 序列化就是将对象以字节形式存于他地，也有可能是文件系统。反序列化就是通过这些字节再次将Java对象重新构造出来。
 *    对象的序列化主要有两种用途：
 * 　　1） 把对象的字节序列永久地保存到硬盘上，通常存放在一个文件中；
 * 　　2） 在网络上传送对象的字节序列。
 *  所以实体类实现Serializable接口后我们可以把对象以文件的形式保存在硬盘中，也可以便于进行网络传输
 */


public class User implements Serializable {

    private static final long serialVersionUID = 7631697126193972999L;
    /***
     * seriaVersionUID就用以在反序列化的过程中验证一个序列化对象的发送方(序列化的人)和接收方(反序列化的人)收到的是同一类对象，
     * 即seriaVersionUID是否一致，如果一致，那么我们认为他是同一对象，否则就认为不一致，抛出InvalidClassException异常。
     */
    private long userId;
    private String userName;
    private  String roleIds;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    private Date createTime;

    /**
     *  最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="Asia/Shanghai")
    private Date lastLoginTime;

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

    public String getCreateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        return dateFormat.format(createTime);
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
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
