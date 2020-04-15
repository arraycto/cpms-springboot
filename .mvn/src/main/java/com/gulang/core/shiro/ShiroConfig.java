package com.gulang.core.shiro;

import com.gulang.core.shiro.filter.JwtFilter;
import com.gulang.core.shiro.filter.PermissionFilter;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DESC:
 *  shiro 配置类
 * @author gulang
 * @date 2019/7/19 14:58
 * @Email 1226740471@qq.com
 */
@Configuration
public class ShiroConfig {

    @Bean
    public MyShiroRealm myShiroRealm(){
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        return myShiroRealm;
    }


    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();

        //一定要通过上面的定义来加载自定义realm,否则在自定义realm中无法注入service层
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //自定义拦截器 （shiro其实有默认的拦截器 如：authc ；在这里我们定义自定义拦截器）
        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();

        //访问权限判断过滤器
        filtersMap.put("permit", permissionFilter());

        // 登陆判断过滤器
        filtersMap.put("jwt", jwtFilter());

        shiroFilterFactoryBean.setFilters(filtersMap);

        //拦截器.
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        // 配置不会被拦截的链接 顺序判断，，必须配置到每个静态目录
        filterChainDefinitionMap.put("/static/css/**", "anon");
        filterChainDefinitionMap.put("/static/fonts/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/static/img/**", "anon");
        filterChainDefinitionMap.put("/static/js/**", "anon");

        filterChainDefinitionMap.put("/login/*", "anon");

        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->

        /* 加入自定义过滤器
         * 1.由于我们要做前后端分离，所以这里我们不需要shiro默认的过滤器authc,因为该过滤器在用户没有登陆的时候
         * 会跳转到登陆页，不符合我们的逻辑处理
         * 2.所以这里我们通过自定义一个登陆判断的过滤器login.这样的话如果用户没有登陆我们直接返回json数据
         * */
        filterChainDefinitionMap.put("/**", "jwt,permit");

        //【由于前后端分离，所以这里不需要做跳转，交给前端来做，后端返回对应的状态码】
        // 需要用户认证时自动重定向到指定登入页面
//        shiroFilterFactoryBean.setLoginUrl("/login/login");
        // 登录成功后自动跳转到指定的页面
//        shiroFilterFactoryBean.setSuccessUrl("/index/index");

        //未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/error/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    // 权限判断过滤器
    public PermissionFilter permissionFilter(){

        return new PermissionFilter();
    }

    // 登陆过滤器器
    public JwtFilter jwtFilter(){

        return new JwtFilter();
    }

    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
