package com.yjt.concurrent.netty.day01;

import cn.hutool.core.date.DateUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @ClassName TimeHandler
 * @Description TODO
 * @Author: YM
 * @Version V1.0
 * @Since V1.0
 * @Date: 2019-09-12 09:15
 **/
public class TimeHandler implements Runnable {
    private Socket socket;

    public TimeHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter out = null;
        try {
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String body = null;
            String currentTime = null;
            while (true) {
                body = reader.readLine();
                out = new PrintWriter(this.socket.getOutputStream(), true);
                if (body == null) {
                    break;
                }
                System.out.println("receive request data:" + body);
                currentTime = body.equalsIgnoreCase("sync time") ? DateUtil.now() : "unknown";
                out.println(currentTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
