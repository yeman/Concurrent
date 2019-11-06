package com.yjt.concurrent.netty.day04;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Arrays;

/**
 * TODO
 * ClassName: TimeClient
 * Date: 2019-11-06 21:44
 * author Administrator
 * version V1.0
 */
public class TimeClient {

    public void connect(String host, int port) {
        //客户端线程组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        Thread[] t = new Thread[5];
        for (int i=0;i<t.length;i++){
            int finalPort = port;
            t[i] = new Thread(()->{
                TimeClient client = new TimeClient();
                client.connect(host, finalPort);
            });
        }
        Arrays.stream(t).forEach(Thread::start);

    }
}
