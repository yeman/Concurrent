package com.yjt.concurrent.netty.day07.objectencoder.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * TODO
 * ClassName: SubscribeReqServer
 * Date: 2019-11-10 10:34
 * author Administrator
 * version V1.0
 */
public class SubscribeReqServer {

    public void run(int port) {
        EventLoopGroup main = null;
        EventLoopGroup work = null;
        ServerBootstrap sb = new ServerBootstrap();
        try {
            main = new NioEventLoopGroup();
            work = new NioEventLoopGroup();
            sb.group(main, work).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO))
                                    .addLast(new ObjectDecoder(1024 * 1024, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new SubscribeChannelHandler());
                        }
                    });
            ChannelFuture cf = sb.bind(port).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            work.shutdownGracefully();
            main.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        final int port = 8080;
        SubscribeReqServer server = new SubscribeReqServer();
        server.run(port);
    }
}
