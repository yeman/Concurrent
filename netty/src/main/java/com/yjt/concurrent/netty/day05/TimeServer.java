package com.yjt.concurrent.netty.day05;

import com.yjt.concurrent.netty.day04.TimeServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * TODO
 * ClassName: TimeServer
 * Date: 2019-11-07 23:03
 * author Administrator
 * version V1.0
 */
public class TimeServer {

    public void run (int port){
        NioEventLoopGroup main = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        try {
            b.group(main,work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeServerHandler5());
                        }
                    });
            ChannelFuture f = b.bind(port).sync();

            //等待服务器监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            work.shutdownGracefully();
            main.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        TimeServer server = new TimeServer();
        server.run(port);
    }
}
