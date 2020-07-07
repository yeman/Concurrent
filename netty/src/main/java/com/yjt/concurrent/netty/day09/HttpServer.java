package com.yjt.concurrent.netty.day09;

import com.yjt.concurrent.netty.day09.bean.Person;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * TODO
 * ClassName: HttpServer
 * Date: 2019-11-21 21:36
 * author Administrator
 * version V1.0
 */
public class HttpServer {

    public void connect(final int port) {
        NioEventLoopGroup main = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(main, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new HttpRequestDecoder())
                                    .addLast(new HttpObjectAggregator(65536))
                                    .addLast(new HttpXmlRequestDecoder(Person.class))
                                    .addLast(new HttpResponseEncoder())
                                    .addLast(new HttpXmlResponseEncoder())
                                    .addLast(new HttpXmlServerHandler());

                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(port)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            work.shutdownGracefully();
            main.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        final int port = 8080;
        HttpServer server = new HttpServer();
        server.connect(port);
    }
}
