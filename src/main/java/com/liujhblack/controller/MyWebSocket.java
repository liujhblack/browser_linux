package com.liujhblack.controller;


import ch.ethz.ssh2.*;
import com.alibaba.fastjson.JSONObject;
import com.liujhblack.util.ReadThread;
import com.liujhblack.util.SSHMapUtil;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.ByteBuffer;

@ServerEndpoint("/shell/{randomString}")
@Component
public class MyWebSocket {

    private static Integer count = 0;

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session socketSession;

    private PrintWriter out;

    private ch.ethz.ssh2.Session sshSession;

    private Connection sshconnection;

    /**
     * 连接建立成功调用的方法
     *
     * @param socketSession 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(@PathParam("randomString") String randomString, Session socketSession) throws IOException {
        this.socketSession = socketSession;
        this.sshSession = SSHMapUtil.getSSHSession(randomString);
        this.sshconnection = SSHMapUtil.getSSHConnection(randomString);
        ReadThread readThread1 = new ReadThread(sshSession.getStdout(), this, "UTF-8");
        ReadThread readThread2 = new ReadThread(sshSession.getStderr(), this, "UTF-8");
        readThread1.start();
        readThread2.start();
        out = new PrintWriter(new OutputStreamWriter(sshSession.getStdin(), "UTF-8"));
        System.out.println("建立连接成功,当前会话数量：" + ++count);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        sshSession.close();
        sshconnection.close();
        System.out.println("关闭连接,当前会话数量：" + --count);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject object = JSONObject.parseObject(message);
        String data = object.getString("data");
        if (data == null || data.trim() == null) {
            return;
        }
        out.print(data);
        out.flush();
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * @Author liujunhui
     * @Date 2019/7/3 16:54
     * @Description 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     */
    public void sendBinary(ByteBuffer byteBuffer) throws IOException {
        this.socketSession.getBasicRemote().sendBinary(byteBuffer);
    }

}