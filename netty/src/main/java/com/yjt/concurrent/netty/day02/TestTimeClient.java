package com.yjt.concurrent.netty.day02;

import java.util.concurrent.TimeUnit;

/**
 * TODO
 * ClassName: TestTimeClient
 * Date: 2019-10-29 0:00
 * author Administrator
 * version V1.0
 */
public class TestTimeClient {

    public static void main(String[] args) throws InterruptedException {
        int clientThread = 200;
        final int clientPort = 8080;
        final String host = "127.0.0.1";
        //客户端线程
        TimeClient client = new TimeClient(host,clientPort);
        new Thread(client).start();
        //Thread.sleep(TimeUnit.SECONDS.toSeconds(50));
    }
}
