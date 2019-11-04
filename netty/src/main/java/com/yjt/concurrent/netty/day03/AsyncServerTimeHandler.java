package com.yjt.concurrent.netty.day03;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * TODO
 * ClassName: AsyncTimeServerTimeHandler
 * Date: 2019-10-31 21:28
 * author Administrator
 * version V1.0
 */
public class AsyncServerTimeHandler implements Runnable{
    private  int port;
    protected CountDownLatch countDownLatch;
    protected AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    //1 初始化AsynchronousServerSocketChannel
    //2 绑定服务器端口
    // run 方法条用 accept方法,调用 asynchronousServerSocketChannel.accept
    public AsyncServerTimeHandler(int port) {
        try {
            this.port  = port;
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("Time Server start in port"+port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        doAccept();
        try {
            countDownLatch.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAccept() {
        asynchronousServerSocketChannel.accept(this,new AcceptCompleteHandler());
    }
}
