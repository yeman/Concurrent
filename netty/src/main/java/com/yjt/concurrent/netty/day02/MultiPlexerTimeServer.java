package com.yjt.concurrent.netty.day02;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;

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

    MultiPlexerTimeServer(String host,int port){
        try {
            //1 打开ServerSocketChannel 用于监听所有客户端的连接,它是所有客户端连接的父管道
            serverSocketChannel = ServerSocketChannel.open();
            //2 绑定监听端口,设置连接为非阻塞模式
            serverSocketChannel.bind(new InetSocketAddress (host,port),1024);
            serverSocketChannel.configureBlocking(false);
            //3 创建Reactor线程,创建多路复用器并启动线程
            selector = Selector.open();
            //4 将ServerSocketChannel注册到Reactor线程的多路复用器Selector上,监听Accept事件
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
        // 5 多路复用器在线程run方法的无限循环体内轮训准备就绪的key
        // 6多路复用器监听到新的客户端的接入,处理新的接入请求,完成TCP三次握手,建立物理链路
        // 7 设置客户端链路为非阻塞
        //  8 将 新接入的客户端连接注册到Reactor线程上的多路复用器上,监听读操作,读取客户端发送的消息
        // 9 异步读取客户端的消息到缓冲区中
        //10 对ByteBuffer缓存区的数据进行编解码,如果有半包消息指针reset,继续读取后续的报文,对解码
        // 11 将pojo对象encode成ByteBuffer,调用SocketChannel异步write接口,将消息异步发给客户端
        while(!stop){
            try {
                int num = selector.select();
                System.out.println("num"+ num);
                selector.select(1000);
                Iterator<SelectionKey>  key = selector.selectedKeys().iterator();
                SelectionKey selectionKey = null;
                while(key.hasNext()){
                    selectionKey = (SelectionKey)key.next();
                    //处理io事件
                    key.remove();
                    try {
                        handlerInput(selectionKey);
                    } catch (Exception e) {
                        if(selectionKey!=null){
                            selectionKey.cancel();
                            if(selectionKey.channel().isOpen()){
                                selectionKey.channel().close();
                            }
                        }
                        e.printStackTrace();
                    }
                    //如果多路复用器关闭关闭后,所有注册在上面的Channel和pipe等资源都会自动的去掉注册,所以不用重复的关闭资源
                    if(selector!=null){
                        try {
                            selector.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlerInput(SelectionKey selectionKey) throws IOException {
        //处理新接入的请求消息
        if(selectionKey.isValid()){
           ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
           SocketChannel sc = serverSocketChannel.accept();
           sc.configureBlocking(false);
           sc.register(selector,SelectionKey.OP_READ);
        }
        if(selectionKey.isReadable()){
            SocketChannel sc = (SocketChannel) selectionKey.channel();
            ByteBuffer bf = ByteBuffer.allocate(1024);
            int readBytes = sc.read(bf);
            if(readBytes>0){
                //将缓冲区的当前的limit设置成position,postion设置为0 ,用于后续的缓冲区的读取操作
                // 然后根据缓冲区可读的字节个数创建字节数组
                bf.flip();
               byte[] bytes = new byte[bf.remaining()];
                bf.get(bytes);
                String body = new String(bytes,"UTF-8");
                System.out.println("Time Server receive order :"+body);
                String currentTime = "Current Time".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "Bad order";
                writeToClient(sc,currentTime);

            }else if(readBytes<0){
                selectionKey.cancel();
                sc.close();
            }else{
                //读到0字节,忽略
            }
        }

    }

    private void writeToClient(SocketChannel sc, String resp) throws IOException {
        if(resp!=null && resp.trim().length()>0){
            byte[] respBytes = resp.getBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(respBytes.length);
            byteBuffer.put(respBytes);
            byteBuffer.flip();
            //忽略写半包的场景
            sc.write(byteBuffer);
        }
    }

}
