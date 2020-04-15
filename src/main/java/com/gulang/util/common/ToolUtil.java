package com.gulang.util.common;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/***
 *  通用方法工具类
 */
public class ToolUtil {
    public  static  final String PRIMARYKEY = "cpms"; //密码加密的唯一的salt

    /**
     * response 处理非控制器类的输出JSON 公共函数
     * @param response
     * @param resultMap
     * @throws IOException
     */
    public static void outJson(ServletResponse response, Map<String, Object> resultMap){

        PrintWriter out = null;
        try {
            response.setContentType("application/json;charset=UTF-8"); // 一定要写这个，否则传到前台的是中文乱码
            out = response.getWriter();
            out.println(new JSONObject(resultMap).toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(null != out){
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 判断是否是Ajax请求
     * @param request
     * @return
     */
    public static boolean isAjax(ServletRequest request){
        return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
    }

    /**
     * 加密用户明文密码
     * @param userPwd  用户明文密码
     * @return String
     */
    public static String md5UserPwd(String userPwd){
        String pwd = String.format("%s#%s",PRIMARYKEY,userPwd);
         pwd = DigestUtil.md5Hex(pwd);
        return pwd;
    }


    /**
     *  格式化用户角色操作权限数据结构类型
     * @param roleAuths  数据库取出来的数据
     * @return  Object
     */
    public  static String formateRolePermission(StringBuffer roleAuths){
        String asString = "";
        if(roleAuths.length() > 0) {
            String authString = roleAuths.deleteCharAt(roleAuths.length() - 1).toString();


            List<String> arrayList = new ArrayList<String>();

            // 高效的数组转list集合
            Collections.addAll(arrayList, authString.split(","));

            List<String> setList   = listDuplicateRemoval(arrayList);  //去重
            asString = StringUtils.join(setList.toArray(), ",");

            return asString;
        }
        return asString;
    }


    /**
     * list去重
     * @param list
     * @return`
     */
    public  static List<String> listDuplicateRemoval(List<String> list){

        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    /**
     * 通过请求头的token值，获取用户信息
     * @param token
     * @return Map
     */
    public static Map<String,String> getUserInfoByToken(String token) {
        Map<String,String> userMap = new HashMap<String, String>();
        Claims parseToken = JwtUtil.parseJwtToken(token);
        String uname =  (String)parseToken.get("username");
        String userId    = String.valueOf(parseToken.get("uid"));

        userMap.put("uname",uname);
        userMap.put("userId",userId);
        return userMap;
    }

    /**
     * 迭代删除文件夹几子文件
     * @param dirPath 文件夹路径
     */
    public static void deleteDir(String dirPath)
    {
        File file = new File(dirPath);
        if(file.isFile())
        {
            file.delete();
        }else
        {
            File[] files = file.listFiles();
            if(files == null)
            {
                file.delete();
            }else
            {
                for (int i = 0; i < files.length; i++)
                {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }

}
