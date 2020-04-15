package com.gulang.util.common;

import cn.hutool.crypto.digest.DigestUtil;
import net.sf.json.JSONObject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/***
 *  通用方法工具类
 */
public class ToolUtil {
    public  static  final String PRIMARYKEY = "cpms"; //密码加密的唯一的salt
    /**
     * response 处理过滤是的输出JSON 公共函数
     * @param response
     * @param resultMap
     * @throws IOException
     */
    public static void outJson(ServletResponse response, Map<String, Object> resultMap){

        PrintWriter out = null;
        try {
            response.setContentType("application/json;charset=UTF-8"); // 一定要写这个，否则传到前台的是中文乱码
            out = response.getWriter();
            out.println(JSONObject.fromObject(resultMap).toString());
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
     *  控制器统一的json返回 函数
     * @param code   状态码
     * @param msg    状态码说明
     * @param data   返回数据
     * @return  Map 集合
     */
    public static Map commonResult(Integer code,String msg,Object data) {

        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("code",code);
        resultMap.put("msg",msg);
        resultMap.put("data",data);
        return  resultMap;
    }
}
