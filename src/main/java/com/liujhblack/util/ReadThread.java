package com.liujhblack.util;/**
 * Created by liujunhui on 2019/6/27.
 */


import com.liujhblack.controller.MyWebSocket;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * @Author liujunhui
 * @Date 2019/6/27 10:30
 * @Description TODO
 */
public class ReadThread extends Thread {
    private InputStream in;//输入流

    private MyWebSocket socket;

    private String charset;

    private boolean flag = true;

    public ReadThread(InputStream in, MyWebSocket socket, String charset) {
        this.in = in;
        this.socket = socket;
        this.charset = charset;
    }

    @Override
    public void run() {
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(in, charset));

            char[] chars = new char[8192];
            int count;

            while ((count=br.read(chars)) !=-1 && flag == true) {
                Thread.sleep(10);//TODO 问题：不休眠数据不全
                String temp = String.valueOf(chars, 0, count);
                byte[] bytes = temp.getBytes("utf-8");
                socket.sendBinary(ByteBuffer.wrap(bytes));
            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
