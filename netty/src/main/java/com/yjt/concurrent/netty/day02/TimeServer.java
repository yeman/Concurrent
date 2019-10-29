package com.yjt.concurrent.netty.day02;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName TimeServer
 * @Description TODO
 * @Author: YM
 * @Version V1.0
 * @Since V1.0
 * @Date: 2019-10-22 12:58
 **/
public class TimeServer {
    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        final String host = "127.0.0.1";
        if(args!=null && args.length>0){
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        MultiPlexerTimeServer server = new MultiPlexerTimeServer(host,port);
        new Thread(server,"NIO-MultiPlexerTimeServer-0001").start();
        Thread.sleep(TimeUnit.SECONDS.toSeconds(50));
    }
}
