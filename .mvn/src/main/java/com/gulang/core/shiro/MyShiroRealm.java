package com.gulang.core.shiro;

import com.gulang.model.User;
import com.gulang.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.List;

/**
 * DESC:
 *
 * @author gulang
 * @date 2019/7/19 15:12
 * @Email 1226740471@qq.com
 */
public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;


    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。
    * 这个方法的出发机制是 登入时调用了SecurityUtils.getSubject().login() 方法
    * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        System.out.println("进行身份认证中...");
        //获取用户的输入的账号.
        String userName = (String)token.getPrincipal();
        String password = new String((char[]) token.getCredentials()); //shiro会把密码转为字符，所以这里需要把字符转字符串

        //通过username从数据库中查找 User对象.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法

        User user = userService.queryByName(userName);

//        System.out.println("数据库保存的密码：");

        if(user == null ||  !user.getPassword().equals(password)){
            throw new AccountException("用户名或密码不正确");
        }
        Subject currentUser = SecurityUtils.getSubject();

        // 由于使用token过期时间为2小时，所以把shiro过期时间设置为2两小时
        SecurityUtils.getSubject().getSession().setTimeout(60*60*60*1000*2);

        System.out.println("登录验证已通过...");
        ByteSource credentialsSalt = ByteSource.Util.bytes(userName);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, //这里传入的是user对象，比对的是用户名，直接传入用户名也没错，但是在授权部分就需要自己重新从数据库里取权限
                user.getPassword(), //密码
                credentialsSalt,//
                getName()  //realm name
        );
        return authenticationInfo;
    }

    /* 权限信息，包括角色以及权限
    *  这个方法并不是上面方法认证通过了就会执行的。它的触发机制有几种：控制器方法使用@RequiresPermissions;注解、jsp页面
    *  使用shiro标签，还有基于URL动态权限控制，但是基于URL的权限控制的话我们需要自定义一个过滤器如：PermissionFilter
    * 这个过滤器需要跟在author过滤器后面，只有通过认证才会执行PermissionFilter；
    * 这里会有个问题就是当请求url时经过PermissionFilter过滤器，在过滤器中调用isPermitted（）方法时，都会触发
    * 权限配置的doGetAuthorizationInfo方法，如果角色权限信息保存在数据库的话，服务器压力会很大，在考虑服务器性能的情况下
    * 这部分数据应该保存在缓存中
    * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限验证中...");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //如果身份认证的时候没有传入User对象，这里只能取到userName
        //也就是SimpleAuthenticationInfo构造的时候第一个参数传递需要User对象
        User user  = (User)principals.getPrimaryPrincipal();

        String rolePermission = "/index/index"; //模拟数据库查询出来的用户角色对应的权限
        authorizationInfo.addStringPermission(rolePermission);
        return authorizationInfo;
    }

}