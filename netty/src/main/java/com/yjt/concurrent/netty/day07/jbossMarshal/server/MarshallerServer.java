package com.yjt.concurrent.netty.day07.jbossMarshal.server;

import com.yjt.concurrent.netty.day07.jbossMarshal.MarshallingCodeFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * TODO
 * ClassName: MarshallerServer
 * Date: 2019-11-12 22:19
 * author Administrator
 * version V1.0
 */
public class MarshallerServer {

    public void connect(int port){
        NioEventLoopGroup main = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(main,work).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder())
                                    .addLast(MarshallingCodeFactory.buildMarshallingEncoder())
                                    .addLast(new MarshallerHandler07());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            main.shutdownGracefully();
            work.shutdownGracefully();
        }

    }



    public static void main(String[] args) {
        int port = 8080;
        MarshallerServer server = new MarshallerServer();
        server.connect(port);
    }
}
