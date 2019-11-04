package com.yjt.concurrent.netty.day03;

/**
 * TODO
 * ClassName: TimeServer
 * Date: 2019-10-31 21:25
 * author Administrator
 * version V1.0
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if(args!=null && args.length>0){
            port = Integer.parseInt(args[0]);
        }
        AsyncServerTimeHandler serverTimeHandler = new AsyncServerTimeHandler(port);
        new Thread(serverTimeHandler,"AIO-TIMESERVER").start();
    }
}
