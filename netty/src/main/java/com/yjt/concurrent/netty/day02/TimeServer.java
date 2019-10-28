package com.yjt.concurrent.netty.day02;

/**
 * @ClassName TimeServer
 * @Description TODO
 * @Author: YM
 * @Version V1.0
 * @Since V1.0
 * @Date: 2019-10-22 12:58
 **/
public class TimeServer {
    public static void main(String[] args){
        int port = 8080;
        if(args!=null && args.length>0){
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        MultiPlexerTimeServer server = new MultiPlexerTimeServer(port);
        new Thread(server,"NIO-MultiPlexerTimeServer-0001").start();
    }
}
