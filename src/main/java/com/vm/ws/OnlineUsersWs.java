package com.vm.ws;

import com.alibaba.fastjson.JSON;
import com.vm.dao.po.CustomVmUsers;
import com.vm.dao.po.VmUsers;
import com.vm.utils.ByteConstantVar;
import com.vm.utils.CommonUtil;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint("/ws/user/login/{userId}")
public class OnlineUsersWs extends CommonUtil {

    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    public static Map<Long, OnlineUsersWs> clients = new ConcurrentHashMap<Long, OnlineUsersWs>();

    //websocket的session
    public Session session;
    private Long userId;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("userId") Long userId, Session session) {

        this.session = session;

        this.userId = userId;

        clients.put(userId, this);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        /**
         * 不做任何事情
         */
    }

    /**
     * 发生错误时调用
     *
     * @param error
     */
    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    /********************************************功能性方法****************************************************/

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String msg) throws Exception {
        Message message = JSON.parseObject(msg, Message.class);
        Long userId = message.getFromUserId();
        if (!isNullObject(userId)) {
            //登录
            if (Operation.isLogin(message.getOperation())) {
                OnlineUsersWs onlineUsersWs = clients.get(userId);

                //覆盖原有客户端信息
                clients.put(userId, this);

                //通知原有客户端离线
                message.setOperation(Operation.LOGOUT.getCode());
                sendMessage(JSON.toJSONString(message));
            }
            //注销
            if (Operation.isLogout(message.getOperation())) {
                //移除客户端信息
                clients.remove(userId);
            }
        }


    }

    /**
     * Title:
     * <p>
     * Description:发送信息
     * <p>
     *
     * @param message
     * @throws IOException
     * @author Kor_Zhang
     * @date 2017年10月7日 下午2:20:58
     * @version 1.0
     */
    public void sendMessage(String message) throws IOException {
        this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 定义信息格式
     */
    private class Message {
        private Byte operation;
        private Long fromUserId;

        public Byte getOperation() {
            return operation;
        }

        public void setOperation(Byte operation) {
            this.operation = operation;
        }

        public Long getFromUserId() {
            return fromUserId;
        }

        public void setFromUserId(Long fromUserId) {
            this.fromUserId = fromUserId;
        }
    }

    /**
     * 操作
     */
    private enum Operation {
        LOGIN(ByteConstantVar.ONE, "登录"),
        LOGOUT(ByteConstantVar.TWO, "注销");

        Byte code;

        String msg;

        Operation(Byte code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Byte getCode() {
            return code;
        }

        public void setCode(Byte code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }


        public static final boolean isLogin(Byte code) {
            return LOGIN.getCode().equals(code);
        }

        public static final boolean isLogout(Byte code) {
            return LOGOUT.getCode().equals(code);
        }
    }

}