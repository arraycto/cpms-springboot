package com.gulang.core.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * DESC: 认证
 *
 * @author gulang
 * @date 2019/7/19 15:12
 * @Email 1226740471@qq.com
 */
public class MyShiroRealm<supports> extends AuthorizingRealm {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *  主要是用来进行身份认证的，
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {

        /**
         *  认证通过后，会触发PermissionFilter的isAccessAllowed方法，开始验证权限
         */
        return new SimpleAuthenticationInfo(token,token,"MyShiroRealm");
    }

    /** 权限信息，包括角色以及权限、
    *  在过滤器中调用isPermitted（）方法时，会触发该方法
    *  【
    *       注：权限判断这部分没有用isPermitted（）方法来判断，而是在PermissionFilter过滤器中自定义了权限判断
    *       所以用不到这个方法
    *   】
    * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        return authorizationInfo;
    }


}