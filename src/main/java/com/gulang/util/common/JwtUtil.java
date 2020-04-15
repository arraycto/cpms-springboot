package com.gulang.util.common;

import io.jsonwebtoken.*;

import java.util.*;

/**
 * des: 编写JwtUtil工具类（生成token, 解析校验token）
 */
public class JwtUtil   {

    /*
     *创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
     *生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。
    */
    private  static  final String TOKENKEY =  "cpms7ba08e055b00626ac575e2dbee3cabb1"; //token密钥

    private  static  final String ISSUER   =  "cpms admin"; //jwt签发人

    //定义JWT的有效时长
    private  static  final int TOKEN_VAILDITY_TIME = 60*60*1000*2; //有效时间(单位：毫秒，默认有效时长2小时)

    // token 过期前多少分钟刷新token
    private  static  final int TOKEN_EXP_BEFORE_TIME  = 30*60*1000; // (单位：毫秒，默认离过期30分钟可以刷新token）

    // 允许的过期时间
    private  static  final SignatureAlgorithm TOKEN_SIGNATURE  = SignatureAlgorithm.HS256; // JWT签名算法

    /**
     * 用户登录成功后生成Jwt
     * 使用Hs256算法
     * @param subject      可以携带用户的相关信息 比如：用户名 用户ID 用户角色对应的权限
     * @return
     */
    public static String createJWT(Map<String,Object> subject) {
        //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成JWT的时间
        long nowMillis = System.currentTimeMillis(); // 毫秒值
        Date exp = new Date(nowMillis+TOKEN_VAILDITY_TIME); // 过期时间

        //下面就是在为payload添加各种标准声明和私有声明了
        //这里其实就是new一个JwtBuilder，设置jwt的body
        String builder = Jwts.builder()
                // sbuject 保存一些用户的信息到jwt中,setClaims()必须放到前面，否则会被覆盖
                .setClaims(subject)
                //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                //iat: jwt的签发时间
                .setIssuedAt(new Date())
                //jwt签发人
                .setIssuer(ISSUER)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(TOKEN_SIGNATURE, TOKENKEY)
                //设置过期时间
                .setExpiration(exp)
                .compact();
        return builder;
    }

    /**
     * Token的解析，这个过程也是一个token的验证过程，当token过期或 签名不正确会抛出错误异常，利用异常给客户端提示
     *  相对应的信息
     * @param token http 头部token信息
     * @return
     */
    public static Claims parseJwtToken(String token) {

        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(TOKENKEY)
                //设置需要解析的jwt
                .parseClaimsJws(token).getBody();

        return claims;
    }


    /**
     *  刷新token值
     * @param token
     * @return
     */
    public  static  String refreshToken(String token)  {

        Claims parseToken = parseJwtToken(token); // 解析旧的token，获取用户信息，创建新的token

        Map<String,Object> tokenMap = new HashMap<String, Object>();

        tokenMap.put("uid",String.valueOf(parseToken.get("uid")));
        tokenMap.put("username",parseToken.get("username"));
        tokenMap.put("roleHandlePermission",(String)parseToken.get("roleHandlePermission"));

        String newJwt = createJWT(tokenMap);

        return newJwt;

    }

    /**
     * des : 检查token合法性并判断有效期时间，决定是否刷新token
     * @param token
     * @return
     */
    public  static boolean checkTokenExpire(String token){
        Claims jwtToken = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(TOKENKEY)
                .parseClaimsJws(token)
                .getBody(); // 从旧的token中解析出用户信息用于新token的生成


        // 获取token过期的时间戳
        long expiresAt = jwtToken.getExpiration().getTime();

        // 判断是否需要刷新token值
        if( 0< expiresAt - System.currentTimeMillis() &&
                expiresAt - System.currentTimeMillis() <= TOKEN_EXP_BEFORE_TIME) {

            return  true;

        }

        return false;
    }


}
