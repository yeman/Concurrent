package com.yjt.concurrent.netty.day01;

import com.yjt.concurrent.netty.day01.TimeHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName TimeServer
 * @Description TODO
 * @Author: YM
 * @Version V1.0
 * @Since V1.0
 * @Date: 2019-09-12 09:04
 **/
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        ServerSocket server = null;
        Socket socket = null;
        try {
            server = new ServerSocket(port);
            System.out.println("server socket start with port:" + port);
            while (true){
                socket = server.accept();
                new Thread(new TimeHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                    System.out.println("server close ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                server = null;
            }

        }

    }
}
