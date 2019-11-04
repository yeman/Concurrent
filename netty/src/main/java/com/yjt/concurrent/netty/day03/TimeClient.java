package com.yjt.concurrent.netty.day03;

/**
 * TODO
 * ClassName: TimeServer
 * Date: 2019-10-31 21:25
 * author Administrator
 * version V1.0
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        String host = "127.0.0.1";
        if(args!=null && args.length>0){
            port = Integer.parseInt(args[0]);
        }
        AsyncTimeClientHandler clientHandler = new AsyncTimeClientHandler(host,port);
        new Thread(clientHandler,"AIO-CLIENT").start();
    }
}
