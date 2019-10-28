package com.yjt.concurrent.netty.day02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @ClassName MultiPlexerTimeServer
 * @Description TODO
 * @Author: YM
 * @Version V1.0
 * @Since V1.0
 * @Date: 2019-10-22 13:11
 **/
public class MultiPlexerTimeServer implements Runnable{
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private transient boolean stop;

    MultiPlexerTimeServer(int port){
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(InetSocketAddress.createUnresolved("127.0.0.1",port),1024);
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("启动服务端注册成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setStop(){
        this.stop = true;
    }
    @Override
    public void run() {
        while(!stop){
            try {
                selector.select(1000);
                Iterable  key = selector.selectedKeys().iterator();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
