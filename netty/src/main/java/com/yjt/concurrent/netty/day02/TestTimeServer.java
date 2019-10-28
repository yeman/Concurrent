package com.yjt.concurrent.netty.day02;

/**
 * TODO
 * ClassName: TestTimeServer
 * Date: 2019-10-29 0:00
 * author Administrator
 * version V1.0
 */
public class TestTimeServer {

    public static void main(String[] args) {
        int serverThread = 2;
        int clientThread = 200;
        final int port = 8080;
        final int clientPort = 8081;
        final String host = "127.0.0.1";
       for (int i=0;i<serverThread;i++){
            new Thread(()->{
                MultiPlexerTimeServer server = new MultiPlexerTimeServer(host,port);
            },"服务器线程"+i).start();
        }
        //客户端线程
        for (int i=0;i<clientThread;i++){
            new Thread(()->{
                TimeClient client = new TimeClient(host,clientPort);
            },"客户端线程"+i).start();
        }
    }
}
