package com.yjt.concurrent.netty.day02;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * TODO
 * ClassName: TimeClient
 * Date: 2019-10-28 22:25
 * author Administrator
 * version V1.0
 */
public class TimeClient implements Runnable{
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile  boolean stop;
    private String host;
    private int port;
    // 1 打开SocketChannel 绑定客户端本地地址,
    //2 设置SocketChannel为非阻塞模式,同时设置客户端连接的TCP参数,
    // 3 异步连接服务端
    // 4 判断是否连接成功,如果连接成功,则直接注册读状态到多路复用器中,
    // 如果当前没有连接成功(异步连接返回false,说明客户端已经发送异步包,服务端没有ack包,则物理链路还没有建立)
    //5 向 reactor线程的多路复用器注册OP_CONNNECT,监听服务端的TCP_ACK的应答
    // 6 创建Reactor线程,创建多路复用器并启动线程
    //7 多路复用器在线程run方法的无限循环体内轮训就绪的key
    //8 接收connect事件的处理,
    //9 判断连接结果,如果连接成功,注册读事件到多路复用器
    //10 注册读事件到多路复用器
    //11 异步读取客户端的请求消息到缓冲区
    // 12 ByteBuffer缓存区的数据进行编解码,如果有半包消息指针reset,继续读取后续的报文,对解码成功的消息封装成task,投递到业务线程池中
    // 13 将pojo对象encode成ByteBuffer,调用SocketChannel的异步write接口,将消息异步发送给客户端
    public TimeClient(String host,int port){
        this.host = host==null? "127.0.0.1": host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!stop){
            try {
                selector.select(1000);
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                SelectionKey key = null;
                while(keys.hasNext()){
                    key = keys.next();
                    keys.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if(key!=null){
                            key.cancel();
                            if(key.channel()!=null){
                                key.channel().close();
                            }
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            if(selector!=null){
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()){
           SocketChannel sc = (SocketChannel) key.channel();
           if(key.isConnectable()){
               if(sc.finishConnect()){
                   sc.register(selector,SelectionKey.OP_READ);
                   doWrite(sc);
               }else{
                   System.exit(1);
               }

               if(key.isReadable()){
                   ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                   int readBytes = sc.read(readBuffer);
                   if(readBytes>0){
                       readBuffer.flip();
                       byte[] bytes = new byte[readBuffer.remaining()];
                       readBuffer.get(bytes);
                       String body = new String(bytes,"UTF-8");
                       System.out.println("---> body"+body);
                       this.stop = true;
                   }else if (readBytes<0){
                       key.cancel();
                       sc.close();
                   }else{
                       //do nothting
                   }

               }
           }
        }
    }

    private void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] req = "Current Time".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        if(!writeBuffer.hasRemaining()){
            System.out.println("发送请求到服务器成功");
        }
    }

    private void doConnect() throws IOException {
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector,SelectionKey.OP_READ);
            doWrite(socketChannel);
        }else{
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }

    }
}
