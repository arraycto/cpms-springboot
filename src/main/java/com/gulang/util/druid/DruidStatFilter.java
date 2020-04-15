package com.gulang.util.druid;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;

/**
 * DESC:
 *
 * @author gulang
 * @date 2019/7/17 20:12
 * @Email 1226740471@qq.com
 */
@WebFilter(filterName="druidWebStatFilter")
public class DruidStatFilter extends WebStatFilter {

}
