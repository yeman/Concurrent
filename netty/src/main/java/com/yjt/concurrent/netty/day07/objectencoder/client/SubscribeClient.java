package com.yjt.concurrent.netty.day07.objectencoder.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * TODO
 * ClassName: SubscribeClient
 * Date: 2019-11-10 11:10
 * author Administrator
 * version V1.0
 */
public class SubscribeClient {

    public void run(String host,int port){
        NioEventLoopGroup work  = new NioEventLoopGroup();
        try {

            Bootstrap bs = new Bootstrap();
            bs.group(work).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                     .handler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel ch) throws Exception {
                             ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO))
                                      .addLast(new ObjectDecoder(1024, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
                                     .addLast(new ObjectEncoder())
                                      .addLast(new SubscribeClientHandler());
                         }
                     });
            ChannelFuture cf = bs.connect(host,port).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            work.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        final String host = "127.0.0.1";
        final int port = 8080;
        SubscribeClient client = new SubscribeClient();
        client.run(host,port);
    }
}
