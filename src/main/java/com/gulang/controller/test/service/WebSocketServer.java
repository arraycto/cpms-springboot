package com.gulang.controller.test.service;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * des : 需要在shiro配置文件 配置websocket请求免校验
 */
@RestController
@ServerEndpoint(value = "/websocket/{userName}")
public class WebSocketServer {
    private static  final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);

    private String userName;  // 当前连接的用户名

    private Session session;  // 当前连接的客户端session会话

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSockets = new CopyOnWriteArraySet<WebSocketServer>();

    @OnOpen
    public   void  onOpen(@PathParam("userName") String userName, Session session) {
        this.userName = userName;
        this.session = session;
        /*
         * 【注意】
         * 虽然在springboot中 @Component 注解默认是单例的，但是由于这里是websokcet连接，所以最后每个请求进来都会生成一个新的bean,
         * 因此this是不相同的，所以可以使用集合把每个请求过来生成的this实例保存起来
         */
        webSockets.add(this);
        OnlineCount.incrementAndGet();
        this.sendLoginNotice(userName);

    }


    @OnClose
    public void onClose(Session session) {
        webSockets.remove(this);
        OnlineCount.decrementAndGet();
        logger.info("【websocket消息】连接断开, 总数:{}", webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {
        logger.info("【websocket消息】收到客户端发来的消息:{}", message);
        this.sendMsgToAllUser(message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("=================发生错误=====================");
        try {
            this.session.getBasicRemote().sendText("发生连接错误");

        } catch (IOException e) {
            logger.error("=================发生连接错误===================");
            logger.error(e.getMessage(),e);
        }
        error.printStackTrace();
    }


    public void sendLoginNotice(String userName){
        String msg = userName + "上线啦~~~\n";

        this.sendMsgToAllUser(msg);
    }


    /**
     * 点对点广播消息
     * @param toUserName 接收用户
     * @param fromUserName 发送用户
     * @param msg 消息
     */
    public   void sendMsgToUserName(String fromUserName,String toUserName,String msg){

        for (WebSocketServer webSocket: webSockets) {
            logger.info("【websocket消息】点对点广播消息, userName={},message={}", toUserName,msg);
            try {
                // 未做处理
                if(webSocket.userName.equals(toUserName)){

                    webSocket.session.getBasicRemote().sendText("点对点广播");
                }

            } catch (Exception e) {
                logger.error("=================websocket点对点广播消息出错===================");
                logger.error(e.getMessage(),e);
            }
        }
    }

    /**
     * 全体广播消息
     * @param msg 消息
     */
    public void   sendMsgToAllUser(String msg){

        JSONObject sendMsg = new JSONObject(); // fastjson 依赖库

        sendMsg.put("msg",msg);

        for (WebSocketServer webSocket: webSockets) {
            try {

                webSocket.session.getBasicRemote().sendText(sendMsg.toJSONString());

            } catch (Exception e) {
                logger.error("=================websocket全体广播消息出错===================");
                logger.error(e.getMessage(),e);
            }
        }
    }
}
