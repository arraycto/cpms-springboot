package com.gulang.util.druid;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebServlet;

/**
 * DESC:
 * Druid数据库监测 通过 http://127.0.0.1:8080/druid/datasource.html 可以访问druid监控页面 登入密码已在yml文件配置
 * @author gulang
 * @date 2019/7/17 20:06
 * @Email 1226740471@qq.com
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/druid/*")
 public class DruidStatViewServlet extends StatViewServlet {

}
